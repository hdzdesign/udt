package chc.tfm.udt.convertidores;

import chc.tfm.udt.DTO.Donacion;
import chc.tfm.udt.DTO.Jugador;
import chc.tfm.udt.entidades.DonacionEntity;
import chc.tfm.udt.entidades.JugadorEntity;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
//Clse que convierte los DTO a Entity para ser insertado, y luego lo devuelve como DTO
@Converter
@Component("JugadorConverter")
public class JugadorConverter implements AttributeConverter<Jugador, JugadorEntity> {

    @Autowired
    @Qualifier("DonacionConverter")
    private DonacionConverter donacionConverter;

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    // Conversor de Jugador a JugadorEntity
    @Override
    public JugadorEntity convertToDatabaseColumn(Jugador attribute) {
        JugadorEntity e = new JugadorEntity();
        e.setId(attribute.getId());
        e.setDni(attribute.getDni());
        e.setNombre(attribute.getNombre());
        e.setApellido1(attribute.getApellido1());
        e.setApellido2(attribute.getApellido2());
        e.setTelefono(attribute.getTelefono());
        e.setMail(attribute.getMail());
        e.setNacimiento(attribute.getNacimiento());
        e.setEdad(attribute.getEdad());
        e.setNacionalidad(attribute.getNacionalidad());
        e.setInscripcion(attribute.getInscripcion());
        e.setDorsal(attribute.getDorsal());
        e.setFoto(attribute.getFoto());
        List<Donacion> donaciones = attribute.getDonaciones();
        LOG.info(new Gson().toJson(donaciones));
        e.setDonaciones(donaciones != null
                ? donaciones
                        .stream()
                        .map(j -> this.donacionConverter.convertToDatabaseColumn(j))
                        .collect(Collectors.toList())
                : new ArrayList<>());
        return e;
    }
    //Convertir de JugadorEntity a Jugador.
    @Override
    public Jugador convertToEntityAttribute(JugadorEntity dbData) {
        Jugador j = new Jugador();
        j.setId(dbData.getId());
        j.setNombre(dbData.getNombre());
        j.setApellido1(dbData.getApellido1());
        j.setApellido2(dbData.getApellido2());
        j.setTelefono(dbData.getTelefono());
        j.setMail(dbData.getMail());
        j.setNacimiento(dbData.getNacimiento());
        j.setEdad(dbData.getEdad());
        j.setNacionalidad(dbData.getNacionalidad());
        j.setInscripcion(dbData.getInscripcion());
        j.setDorsal(dbData.getDorsal());
        j.setFoto(dbData.getFoto());
        LOG.info("Porque no seteas");
        List<DonacionEntity> donaciones = dbData.getDonaciones();
        j.setDonaciones(donaciones != null
                ? donaciones
                    .stream()
                    .map(d -> this.donacionConverter.convertToEntityAttribute(d))
                    .collect(Collectors.toList())
                : new ArrayList<>());
        LOG.info(j.toString());
        return j;
    }
}
