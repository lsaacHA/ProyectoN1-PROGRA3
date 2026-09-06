package proyecto.logic;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import proyecto.data.LocalDateAdapter;
import proyecto.data.LocalTimeAdapter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class SolicitudReserva {
    @XmlIDREF
    private Funcionario funcionario;
    private String actividad;
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate fecha;
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    private LocalTime horaInicio;
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    private LocalTime horaFin;
    @XmlElementWrapper(name = "categoriasSolicitadas")
    @XmlElement(name = "categoria")
    @XmlIDREF
    private List<Categoria> categorias;

    public SolicitudReserva() {
        categorias = new ArrayList<>();
    }

    public SolicitudReserva(Funcionario funcionario, String actividad,
                            LocalDate fecha, LocalTime horaInicio, LocalTime horaFin,
                            List<Categoria> categorias) {
        this.funcionario = funcionario;
        this.actividad = actividad;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.categorias = categorias;
    }

    public Funcionario getFuncionario() { return funcionario; }
    public void setFuncionario(Funcionario funcionario) { this.funcionario = funcionario; }
    public String getActividad() { return actividad; }
    public void setActividad(String actividad) { this.actividad = actividad; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }
    public List<Categoria> getCategorias() { return categorias; }
    public void setCategorias(List<Categoria> categorias) { this.categorias = categorias; }
}
