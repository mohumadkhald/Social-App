package com.projects.socialapp.service;

import com.projects.socialapp.Repo.UserRepo;
import com.projects.socialapp.expection.EmailAlreadyExistsException;
import com.projects.socialapp.expection.UserNotFoundException;
import com.projects.socialapp.mapper.UserMapper;
import com.projects.socialapp.model.User;
import com.projects.socialapp.requestDto.UserRequestDto;
import com.projects.socialapp.responseDto.UserResponseDto;
import com.projects.socialapp.traits.ApiTrait;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    /*
    |--------------------------------------------------------------------------
    | Inject Of  Class
    |--------------------------------------------------------------------------
    |
    | Here is where you can Inject Class Service and Repository and Another Providers
    |
    */
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final ApiTrait apiTrait;

    /*|--------------------------------------------------------------------------
            | End of Inject
    |-------------------------------------------------------------------------- */




    /*|--------------------------------------------------------------------------
        | Start Implements All Method
    |-------------------------------------------------------------------------- */

    /*
    |--------------------------------------------------------------------------
    | Create New User
    |--------------------------------------------------------------------------
    |
    | Here is How you can Create New account For User
    |
    */
    @Override
    public UserResponseDto registerUser(UserRequestDto dto) {
        // Check if the email already exists
        if (userRepo.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        var studentModel = userMapper.toUser(dto);
        var savedStudentModel = userRepo.save(studentModel);
        return userMapper.toUserResponseDto(savedStudentModel);
    }

    /*|--------------------------------------------------------------------------
        | End Implement
    |-------------------------------------------------------------------------- */



    /*
    |--------------------------------------------------------------------------
    | Implement Of Retrieve User
    |--------------------------------------------------------------------------
    |
    | Here is How you can Get user by ID
    |
    */
    @Override
    public ResponseEntity<?> getUserByIdResponse(Long id) {
        List<UserResponseDto> user = Collections.singletonList(findUserById(id));
        return apiTrait.data(user,"The Data For User Retrieved Success", HttpStatus.OK);

    }

    private UserResponseDto findUserById(Long id) {
        Optional<User> userOptional = userRepo.findById(id);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        return userOptional.map(userMapper::toUserResponseDto).orElse(null);
    }



    /*
    |--------------------------------------------------------------------------
    | Implement How Can Search Email
    |--------------------------------------------------------------------------
    |
    | Here is How you can Search about User using Email Address
    |
    */
    @Override
    public ResponseEntity<?> getUserByEmailResponse(String email) {
        UserResponseDto user = findUserByEmail(email);
        return apiTrait.data(Collections.singletonList(user), "The Data For User Retrieved Success", HttpStatus.OK);
    }

    private UserResponseDto findUserByEmail(String email) {
        User user = userRepo.findByEmail(email);
        if (user != null) {
            return userMapper.toUserResponseDto(user);
        }
        throw new UserNotFoundException("The user not found" + email);
    }

    /*|--------------------------------------------------------------------------
            | End Implement
    |-------------------------------------------------------------------------- */



    /*
    |--------------------------------------------------------------------------
    | Implement Followers
    |--------------------------------------------------------------------------
    |
    | Here is How you can get all Users Using List And Hash Map
    |
    */
    @Override
    public ResponseEntity<?> followUser(Long userId1, Long userId2) {
        User user1 = userRepo.findUserById(userId1);
        User user2 = userRepo.findUserById(userId2);
        user2.getFollowers().add(Math.toIntExact(user1.getId()));
        user1.getFollowings().add(Math.toIntExact(user2.getId()));
        userRepo.save(user1);
        userRepo.save(user2);
        return apiTrait.successMessage("followers add success",HttpStatus.ACCEPTED);
    }
    /*|--------------------------------------------------------------------------
            | End Implement
    |-------------------------------------------------------------------------- */



    /*
    |--------------------------------------------------------------------------
    | Implement How Can Search about User
    |--------------------------------------------------------------------------
    |
    | Here is How you can get all Users Using List And Hash Map
    |
    */
    @Override
    public List<User> searchUser(String query) {
        return null;
    }
    /*|--------------------------------------------------------------------------
            | End Implement
    |-------------------------------------------------------------------------- */



    /*
    |--------------------------------------------------------------------------
    | Implement How Can Get All Users
    |--------------------------------------------------------------------------
    |
    | Here is How you can get all Users Using List And Hash Map After Get Collectors
    |
    */
    @Override
    public ResponseEntity<?> getAllUsers() {
        List<UserResponseDto> users = findAllUsers();
        if (users.isEmpty()) {
            return apiTrait.errorMessage(new HashMap<>(), "No users found", HttpStatus.NOT_FOUND);
        } else {
            return apiTrait.data(users, "The Data Retrieved Success", HttpStatus.OK);
        }
    }

    private List<UserResponseDto> findAllUsers() {
        return userRepo.findAll()
                .stream()
                .map(userMapper::toUserResponseDto)
                .collect(Collectors.toList());
    }

    /*|--------------------------------------------------------------------------
            | End Implement
    |-------------------------------------------------------------------------- */



    /*
    |--------------------------------------------------------------------------
    | Implement How Can Edit User
    |--------------------------------------------------------------------------
    |
    | Here is How you can Edit user and make all checks if email exist
    |
    */
    @Override
    public ResponseEntity<?> updateUser(Long id, UserRequestDto dto) {
        try {
            // Retrieve the user by id
            User user = userRepo.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

            // Check if the email is being updated
            if (!user.getEmail().equals(dto.getEmail())) {
                // If the email is being updated, check if the new email already exists
                User existingUserByEmail = userRepo.findByEmail(dto.getEmail());
                if (existingUserByEmail != null && !existingUserByEmail.getId().equals(id)) {
                    // If the new email already exists and belongs to a different user, throw an exception
                    throw new EmailAlreadyExistsException("Email Already Exists: " + dto.getEmail());
                }
            }

            // Update user fields
            user.setFirstname(dto.getFirstname());
            user.setLastname(dto.getLastname());
            user.setGender(dto.getGender());
            user.setEmail(dto.getEmail());
            user.setPassword(dto.getPassword());
            // Save the updated user
            userRepo.save(user);


            // Return success response
            return apiTrait.successMessage("User updated successfully", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            // Return error response for user not found
            throw  new UserNotFoundException("The User Not Found" + id);
        } catch (EmailAlreadyExistsException e) {
            // Return error response for email already exists
            return apiTrait.errorMessage(null, e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Return error response for unexpected errors
            return apiTrait.errorMessage(null, "An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /*|--------------------------------------------------------------------------
            | End Implement
    |-------------------------------------------------------------------------- */


}
