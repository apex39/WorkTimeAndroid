package bak.mateusz.worktime.network;

import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by timo on 08.02.17.
 */

public interface WorkTimeClient {
    @POST("/users/{user}/repos")
    Call<List<GitHubRepo>> reposForUser(
            @Path("user") String user
    );
}
