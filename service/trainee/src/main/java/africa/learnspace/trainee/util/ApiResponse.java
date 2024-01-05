package africa.learnspace.trainee.util;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@Builder
public class ApiResponse {
    private Object data;
    private boolean isSuccessful;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiResponse that = (ApiResponse) o;
        return Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }



}

