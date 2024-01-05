package africa.learnspace.trainee.util;

public class GenerateResponse {
    public static ApiResponse createdResponse(Object response){
        return ApiResponse.builder()
                .data(response)
                .isSuccessful(true)
                .build();
    }

    public static ApiResponse okResponse(Object response){
        return ApiResponse.builder()
                .data(response)
                .isSuccessful(true)
                .build();
    }
}