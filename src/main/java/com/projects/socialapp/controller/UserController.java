package com.projects.socialapp.controller;

import com.projects.socialapp.Repo.UserRepo;
import com.projects.socialapp.model.User;
import com.projects.socialapp.requestDto.UserRequestDto;
import com.projects.socialapp.responseDto.UserProfileDto;
import com.projects.socialapp.responseDto.UserResponseDto;
import com.projects.socialapp.service.UserService;
import com.projects.socialapp.traits.ApiTrait;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserRepo userRepo;
    private final UserService userService;
    private final ApiTrait apiTrait;



    /*
    |--------------------------------------------------------------------------
    | API Routes Register
    |--------------------------------------------------------------------------
    |
    | Here is where you can register API routes for your application. These
    | routes are loaded by the RouteServiceProvider and all of them will
    | be assigned to the "api" middleware group. Make something great!
    | after register you will receive token
    |
    */
    @PostMapping
    public UserResponseDto register(@Valid @RequestBody UserRequestDto dto)
    {
        return  this.userService.registerUser(dto);
    }




    /*
    |--------------------------------------------------------------------------
    | API Routes Get User By Id
    |--------------------------------------------------------------------------
    */
    @GetMapping("/{id}")
    public ResponseEntity<?> findUserById(@PathVariable Integer id) throws Exception
    {
        return userService.getUserByIdResponse(id);
    }


    /*
    |--------------------------------------------------------------------------
    | API Routes Search about Email
    |--------------------------------------------------------------------------
    */
    @GetMapping("/search/{email}")
    public ResponseEntity<?> findUserByEmail(@PathVariable String email) throws Exception
    {
        return userService.getUserByEmailResponse(email);
    }



    /*
    |--------------------------------------------------------------------------
    | API Routes Edit Data of User
    |--------------------------------------------------------------------------
    */
    @PutMapping("/{id}")
    public ResponseEntity<?> editUser(@PathVariable Integer id, @RequestBody UserRequestDto newData)
    {

        return userService.updateUser(id, newData);

    }




    /*
    |--------------------------------------------------------------------------
    | API Routes Delete User
    |--------------------------------------------------------------------------
    */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id)
    {
        Optional<User> user = userRepo.findById(id);
        if (user.isPresent())
        {
            userRepo.deleteById(id);
            return ResponseEntity.ok("user deleted");
        }
        return ResponseEntity.ok("user not found");
    }

    /*
    |--------------------------------------------------------------------------
    | API Routes Get AllUser
    |--------------------------------------------------------------------------
    */
    @GetMapping
    public ResponseEntity<?> getUsers()
    {
        return userService.getAllUsers();
    }


    /*
    |--------------------------------------------------------------------------
    | API Routes Follow User
    |--------------------------------------------------------------------------
    */
    @PutMapping("/follow/{userId2}")
    public ResponseEntity<?> followUserHandler(@PathVariable Integer userId2, @RequestHeader("Authorization") String jwtToken)
    {
        Integer userId1 = userService.findUserIdByJwt(jwtToken);
        return userService.followUser(userId1, userId2);
    }


    @PutMapping("/unfollow/{userId2}")
    public ResponseEntity<?> unfollowUserHandler(@PathVariable Integer userId2, @RequestHeader("Authorization") String jwtToken) throws ChangeSetPersister.NotFoundException {
        Integer userId1 = userService.findUserIdByJwt(jwtToken);
        return userService.unfollowUser(userId1, userId2);
    }




    @GetMapping("/{userId}/profile")
    public ResponseEntity<?> getUserProfile(@PathVariable Integer userId) {
        try {
            UserProfileDto userProfileDto = userService.getUserProfile(userId);
            return ResponseEntity.ok(userProfileDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }
}
