package africa.learnspace.traininginstitute.dto.response;

public class GenerateResponse {
    public static ApiResponse createdResponse(Object data){
        return ApiResponse.builder()
                .data(data)
                .isSuccessful(true)
                .build();
    }
    public static ApiResponse okResponse(Object data){
        return ApiResponse.builder()
                .data(data)
                .isSuccessful(true)
                .build();
    }

}
