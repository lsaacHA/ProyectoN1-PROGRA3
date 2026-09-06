package proyecto.data;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import proyecto.logic.Categoria;
import proyecto.logic.Funcionario;
import proyecto.logic.Recurso;
import proyecto.logic.Reserva;
import proyecto.logic.Rol;
import proyecto.logic.Usuario;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "sistemaReservas")
@XmlAccessorType(XmlAccessType.FIELD)
public class Data {
    public static final Path DEFAULT_FILE = Path.of("data", "datos.xml");

    @XmlElementWrapper(name = "usuarios")
    @XmlElement(name = "usuario")
    private List<Usuario> usuarios;

    @XmlElementWrapper(name = "funcionarios")
    @XmlElement(name = "funcionario")
    private List<Funcionario> funcionarios;

    @XmlElementWrapper(name = "categorias")
    @XmlElement(name = "categoria")
    private List<Categoria> categorias;

    @XmlElementWrapper(name = "recursos")
    @XmlElement(name = "recurso")
    private List<Recurso> recursos;

    @XmlElementWrapper(name = "reservas")
    @XmlElement(name = "reserva")
    private List<Reserva> reservas;

    @XmlTransient
    private Path archivo;

    public Data() {
        usuarios = new ArrayList<>();
        funcionarios = new ArrayList<>();
        categorias = new ArrayList<>();
        recursos = new ArrayList<>();
        reservas = new ArrayList<>();
    }

    public static Data load() throws Exception {
        return load(DEFAULT_FILE);
    }

    public static Data load(Path archivo) throws Exception {
        if (Files.notExists(archivo)) {
            Data data = new Data();
            data.archivo = archivo;
            data.usuarios.add(new Usuario("admin", "admin", Rol.ADMINISTRADOR));
            data.store();
            return data;
        }

        JAXBContext context = JAXBContext.newInstance(Data.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Data data = (Data) unmarshaller.unmarshal(archivo.toFile());
        data.archivo = archivo;
        data.initializeMissingLists();
        return data;
    }

    public void store() throws Exception {
        if (archivo == null) {
            archivo = DEFAULT_FILE;
        }
        store(archivo);
    }

    public void store(Path archivo) throws Exception {
        Path parent = archivo.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        JAXBContext context = JAXBContext.newInstance(Data.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(this, archivo.toFile());
        this.archivo = archivo;
    }

    private void initializeMissingLists() {
        if (usuarios == null) usuarios = new ArrayList<>();
        if (funcionarios == null) funcionarios = new ArrayList<>();
        if (categorias == null) categorias = new ArrayList<>();
        if (recursos == null) recursos = new ArrayList<>();
        if (reservas == null) reservas = new ArrayList<>();
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public List<Funcionario> getFuncionarios() {
        return funcionarios;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public List<Recurso> getRecursos() {
        return recursos;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public Path getArchivo() {
        return archivo;
    }
}
