package edu.uni.medihistoria.controlador;

import edu.uni.medihistoria.datos.UsuarioDAO;
import edu.uni.medihistoria.modelo.Modulo;
import edu.uni.medihistoria.modelo.Rol;
import edu.uni.medihistoria.modelo.Usuario;
import edu.uni.medihistoria.seguridad.Auditor;
import edu.uni.medihistoria.seguridad.PasswordUtil;
import edu.uni.medihistoria.seguridad.Sesion;
import edu.uni.medihistoria.util.Alertas;
import edu.uni.medihistoria.util.Validador;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.SQLException;

/** Gestión de usuarios (solo Administrador): alta, edición, desactivación, restablecer y desbloquear. */
public class UsuarioController {
    @FXML private TableView<Usuario> tabla;
    @FXML private TableColumn<Usuario, String> colUsuario;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colRol;
    @FXML private TableColumn<Usuario, String> colEstado;
    @FXML private TableColumn<Usuario, String> colAcceso;

    @FXML private TextField txtUsuario;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private ComboBox<Rol> cmbRol;
    @FXML private CheckBox chkActivo;
    @FXML private Label lblMensaje;
    @FXML private Button btnRestablecer;
    @FXML private Button btnDesbloquear;

    private final UsuarioDAO dao = new UsuarioDAO();
    private Usuario seleccionado;

    @FXML
    private void initialize() {
        Sesion.exigirAcceso(Modulo.USUARIOS);

        colUsuario.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getUsuario()));
        colNombre.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getNombreCompleto()));
        colRol.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getRol().getEtiqueta()));
        colEstado.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getEstado()));
        colAcceso.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getUltimoAccesoTexto()));
        tabla.setPlaceholder(new Label("No hay usuarios registrados."));

        cmbRol.getItems().setAll(Rol.values());
        tabla.getSelectionModel().selectedItemProperty()
                .addListener((obs, anterior, nuevo) -> mostrarEnFormulario(nuevo));

        cargarTabla();
        prepararNuevo();
    }

    private void cargarTabla() {
        try {
            tabla.setItems(FXCollections.observableArrayList(dao.listar()));
        } catch (SQLException e) {
            mostrarError(e);
        }
    }

    private void mostrarEnFormulario(Usuario u) {
        if (u == null) {
            return;
        }
        seleccionado = u;
        txtUsuario.setText(u.getUsuario());
        txtUsuario.setDisable(true);          // el nombre de usuario no se modifica
        txtNombres.setText(u.getNombres());
        txtApellidos.setText(u.getApellidos());
        cmbRol.setValue(u.getRol());
        chkActivo.setSelected(u.isActivo());
        btnRestablecer.setDisable(false);
        btnDesbloquear.setDisable(!u.isBloqueado());
        lblMensaje.setText("");
    }

    private void prepararNuevo() {
        seleccionado = null;
        tabla.getSelectionModel().clearSelection();
        txtUsuario.clear();
        txtUsuario.setDisable(false);
        txtNombres.clear();
        txtApellidos.clear();
        cmbRol.setValue(null);
        chkActivo.setSelected(true);
        btnRestablecer.setDisable(true);
        btnDesbloquear.setDisable(true);
    }

    @FXML
    private void onNuevo() {
        prepararNuevo();
        lblMensaje.setText("");
    }

    @FXML
    private void onGuardar() {
        String usuario = txtUsuario.getText().trim();
        String nombres = txtNombres.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        Rol rol = cmbRol.getValue();

        if (seleccionado == null && !Validador.esUsuarioValido(usuario)) {
            lblMensaje.setText("El usuario debe tener de 4 a 30 caracteres: minúsculas, números, punto o guion bajo.");
            return;
        }
        if (!Validador.esTextoValido(nombres, 60) || !Validador.esTextoValido(apellidos, 60)) {
            lblMensaje.setText("Los nombres y apellidos son obligatorios (máximo 60 caracteres).");
            return;
        }
        if (rol == null) {
            lblMensaje.setText("Seleccione un rol.");
            return;
        }

        try {
            boolean guardado = (seleccionado == null)
                    ? crear(usuario, nombres, apellidos, rol)
                    : editar(nombres, apellidos, rol);
            if (!guardado) {
                return;
            }
            cargarTabla();
            prepararNuevo();
            lblMensaje.setText("Usuario guardado correctamente.");
        } catch (SQLException e) {
            mostrarError(e);
        }
    }

    /** Crea el usuario con una contraseña temporal que el administrador ve una sola vez. */
    private boolean crear(String usuario, String nombres, String apellidos, Rol rol) throws SQLException {
        if (dao.existeUsuario(usuario)) {
            lblMensaje.setText("Ya existe un usuario con ese nombre.");
            return false;
        }
        String temporal = PasswordUtil.generarClaveTemporal();
        String sal = PasswordUtil.generarSal();
        Usuario nuevo = new Usuario(0, usuario, nombres, apellidos, rol, chkActivo.isSelected(), false, true, null);
        int id = dao.insertar(nuevo, PasswordUtil.hash(temporal, sal), sal, true);
        Auditor.registrar(Sesion.getUsuario().getIdUsuario(), "USUARIO_CREADO", "Usuario", id,
                usuario + " (" + rol.getCodigoBd() + ")");
        Alertas.claveTemporal(usuario, temporal);
        return true;
    }

    private boolean editar(String nombres, String apellidos, Rol rol) throws SQLException {
        boolean activo = chkActivo.isSelected();
        Usuario yo = Sesion.getUsuario();

        if (seleccionado.getIdUsuario() == yo.getIdUsuario() && (!activo || rol != Rol.ADMINISTRADOR)) {
            lblMensaje.setText("No puede desactivar su propia cuenta ni quitarse el rol de administrador.");
            return false;
        }
        boolean eraAdminActivo = seleccionado.getRol() == Rol.ADMINISTRADOR && seleccionado.isActivo();
        boolean seguiraAdminActivo = rol == Rol.ADMINISTRADOR && activo;
        if (eraAdminActivo && !seguiraAdminActivo
                && dao.contarAdministradoresActivos(seleccionado.getIdUsuario()) == 0) {
            lblMensaje.setText("Debe existir al menos un administrador activo.");
            return false;
        }

        seleccionado.setNombres(nombres);
        seleccionado.setApellidos(apellidos);
        seleccionado.setRol(rol);
        seleccionado.setActivo(activo);
        dao.actualizar(seleccionado);
        Auditor.registrar(yo.getIdUsuario(), "USUARIO_ACTUALIZADO", "Usuario", seleccionado.getIdUsuario(),
                seleccionado.getUsuario() + " -> rol " + rol.getCodigoBd() + ", " + (activo ? "activo" : "inactivo"));
        return true;
    }

    @FXML
    private void onRestablecer() {
        if (seleccionado == null) {
            return;
        }
        if (seleccionado.getIdUsuario() == Sesion.getUsuario().getIdUsuario()) {
            lblMensaje.setText("Para cambiar su propia contraseña use Sistema > Cambiar contraseña.");
            return;
        }
        if (!Alertas.confirmar("Restablecer contraseña",
                "Se generará una contraseña temporal para " + seleccionado.getUsuario() + ". ¿Continuar?")) {
            return;
        }
        try {
            String temporal = PasswordUtil.generarClaveTemporal();
            String sal = PasswordUtil.generarSal();
            dao.cambiarClave(seleccionado.getIdUsuario(), PasswordUtil.hash(temporal, sal), sal, true);
            Auditor.registrar(Sesion.getUsuario().getIdUsuario(), "CLAVE_RESTABLECIDA", "Usuario",
                    seleccionado.getIdUsuario(), seleccionado.getUsuario());
            String nombre = seleccionado.getUsuario();
            cargarTabla();
            prepararNuevo();
            Alertas.claveTemporal(nombre, temporal);
            lblMensaje.setText("Contraseña restablecida.");
        } catch (SQLException e) {
            mostrarError(e);
        }
    }

    @FXML
    private void onDesbloquear() {
        if (seleccionado == null) {
            return;
        }
        try {
            dao.desbloquear(seleccionado.getIdUsuario());
            Auditor.registrar(Sesion.getUsuario().getIdUsuario(), "CUENTA_DESBLOQUEADA", "Usuario",
                    seleccionado.getIdUsuario(), seleccionado.getUsuario());
            cargarTabla();
            prepararNuevo();
            lblMensaje.setText("Cuenta desbloqueada.");
        } catch (SQLException e) {
            mostrarError(e);
        }
    }

    private void mostrarError(Exception e) {
        Alertas.error("Ocurrió un error al procesar la operación.");
        e.printStackTrace(); // el detalle va a la consola
    }
}
