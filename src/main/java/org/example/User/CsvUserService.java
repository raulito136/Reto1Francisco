package org.example.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvUserService implements UserService{
    public String ruta;

    public CsvUserService(String ruta) {
        this.ruta = ruta;
    }

    @Override
    public List<Usuario> listarUsuarios() {
        try(BufferedReader br=new BufferedReader(new FileReader(ruta))){
            List<Usuario> usuarios=new ArrayList<>();
            String linea="";
            while((linea= br.readLine())!=null){
                String[] separacion=linea.split(",");
                usuarios.add(new Usuario(separacion[0].trim(),separacion[1].trim(),separacion[2].trim()));
            }
            return usuarios;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void registrarUsuario(Usuario usuario) {
        try(BufferedWriter bw =new BufferedWriter(new FileWriter(ruta,true))){
            bw.write(usuario.getId()+","+usuario.getEmail()+","+usuario.getContrasenha());
            bw.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
