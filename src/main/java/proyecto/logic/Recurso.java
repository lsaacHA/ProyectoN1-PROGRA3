package proyecto.logic;

public class Recurso {
   private String ID;
   private Categoria categoria;
   private String descripcion;

   public void Recurso(String ID ,Categoria categoria, String descripcion){
       this.categoria=categoria;
       this.ID=ID;
       this.descripcion=descripcion;
   }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
