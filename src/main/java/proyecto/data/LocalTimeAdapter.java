package proyecto.data;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.time.LocalTime;

public class LocalTimeAdapter extends XmlAdapter<String, LocalTime> {
    @Override
    public LocalTime unmarshal(String value) {
        return value == null || value.isBlank() ? null : LocalTime.parse(value);
    }

    @Override
    public String marshal(LocalTime value) {
        return value == null ? null : value.toString();
    }
}
