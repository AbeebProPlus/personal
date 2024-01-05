package africa.learnspace.loanManagement.utils;

public class GenerateResponse {



    public static ApiResponse okResponse(Object data){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(data); apiResponse.setSuccessful(true);
        return apiResponse;
    }

    public static ApiResponse badResponse(Object data) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(data);
        apiResponse.setSuccessful(false);
        return apiResponse;
    }

}
