package com.projects.socialapp.service;

import com.projects.socialapp.Repo.UserRepo;
import com.projects.socialapp.Config.JwtService;
import com.projects.socialapp.expection.EmailAlreadyExistsException;
import com.projects.socialapp.expection.UserNotFoundException;
import com.projects.socialapp.mapper.UserMapper;
import com.projects.socialapp.model.User;
import com.projects.socialapp.requestDto.RegisterRequestDto;
import com.projects.socialapp.responseDto.UserProfileDto;
import com.projects.socialapp.responseDto.UserResponseDto;
import com.projects.socialapp.traits.ApiTrait;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
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
    private final JwtService jwtProvider;
    private final PasswordEncoder passwordEncoder;


    /*|--------------------------------------------------------------------------
            | End of Inject
    |-------------------------------------------------------------------------- */




    /*|--------------------------------------------------------------------------
        | Start Implements All Method
    |-------------------------------------------------------------------------- */

    @Override
    public Integer findUserIdByJwt(String jwt)
    {
        String email = jwtProvider.getEmailFromToken(jwt);
        User user = userRepo.findByEmail(email);

        if (user == null) {
            return null;
        }

        return user.getId();
    }

    @Override
    public User findById(Integer id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }


    /*|--------------------------------------------------------------------------
                                    | End Implement
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
    public UserResponseDto registerUser(RegisterRequestDto dto) {
        if (userRepo.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        var user = userMapper.toUser(dto);
        userRepo.save(user);
        return userMapper.toUserResponseDto(user);
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
    public ResponseEntity<?> getUserByIdResponse(Integer id) {
        List<UserResponseDto> user = Collections.singletonList(findUserById(id));
        return ApiTrait.data(user,"The Data For User Retrieved Success", HttpStatus.OK);

    }

    private UserResponseDto findUserById(Integer id) {
        Optional<User> userOptional = userRepo.findById(id);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        return userOptional.map(userMapper::toUserResponseDto).orElse(null);
    }

    /*|--------------------------------------------------------------------------
                                    | End Implement
    |-------------------------------------------------------------------------- */




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
        return ApiTrait.data(Collections.singletonList(user), "The Data For User Retrieved Success", HttpStatus.OK);
    }

    private UserResponseDto findUserByEmail(String email) {
        User user = userRepo.findByEmail(email);
        if (user != null) {
            return userMapper.toUserResponseDto(user);
        }
        throw new UserNotFoundException("The user not found " + email);
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
    public ResponseEntity<?> searchUser(String query) {
        List<UserResponseDto> users = findUsers(query);
        if (users.isEmpty()) {
            return ApiTrait.errorMessage(new HashMap<>(), "No users found", HttpStatus.NOT_FOUND);
        } else {
            return ApiTrait.data(users, "The Data Retrieved Success", HttpStatus.OK);
        }
    }

    private List<UserResponseDto> findUsers(String query) {
        return userRepo.searchUser(query)
                .stream()
                .map(userMapper::toUserResponseDto)
                .collect(Collectors.toList());
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
            return ApiTrait.errorMessage(new HashMap<>(), "No users found", HttpStatus.NOT_FOUND);
        } else {
            return ApiTrait.data(users, "The Data Retrieved Success", HttpStatus.OK);
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
    public ResponseEntity<?> updateUser(Integer id, RegisterRequestDto dto) {
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
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            // Save the updated user
            userRepo.save(user);


            // Return success response
            return ApiTrait.successMessage("User updated successfully", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            // Return error response for user not found
            throw  new UserNotFoundException("The User Not Found" + id);
        } catch (EmailAlreadyExistsException e) {
            // Return error response for email already exists
            return ApiTrait.errorMessage(null, e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Return error response for unexpected errors
            return ApiTrait.errorMessage(null, "An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*|--------------------------------------------------------------------------
                                    | End Implement
    |-------------------------------------------------------------------------- */




   /*
   |--------------------------------------------------------------------------
   | Implement Follow or UnFollow
   |--------------------------------------------------------------------------
   |
   | Make Follow from user login to another user you need
   |
   */
   @Override
   public ResponseEntity<?> toggleFollowUser(Integer userId1, Integer userId2) {
       User user1 = userRepo.findUserById(userId1);
       User user2 = userRepo.findUserById(userId2);

       if (Objects.equals(userId1, userId2)) {
           return ApiTrait.errorMessage(null, "Cannot follow/unfollow yourself", HttpStatus.BAD_REQUEST);
       } else if (user1 != null && user2 != null) {
           // If user1 is following user2, unfollow; else follow
           if (user1.getFollowings().contains(user2)) {
               // User1 is following user2, so unfollow
               user1.getFollowings().remove(user2);
               user2.getFollowers().remove(user1);
               // Save changes
               userRepo.save(user1);
               userRepo.save(user2);
           } else {
               // User1 is not following user2, so follow
               user1.getFollowings().add(user2);
               user2.getFollowers().add(user1);
               // Save changes
               userRepo.save(user1);
               userRepo.save(user2);
           }

           // Return success message
           String action = user1.getFollowings().contains(user2) ? "followed" : "unfollowed";
           return ApiTrait.successMessage("User " + action + " successfully", HttpStatus.ACCEPTED);
       } else {
           return ApiTrait.errorMessage(null, "User(s) not found", HttpStatus.NOT_FOUND);
       }
   }

    /*|--------------------------------------------------------------------------
                                    | End Implement
    |-------------------------------------------------------------------------- */




    /*
   |--------------------------------------------------------------------------
   | Implement Profile
   |--------------------------------------------------------------------------
   |
   | Get Number of Followers And Following and Get Friends
   |
   */
    @Override
    public ResponseEntity<?> getUserProfile(Integer userId) {
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Calculate the counts of followers and following
            int followersCount = user.getFollowers().size();
            int followingCount = user.getFollowings().size();

            // Calculate the count of friends (mutual follows)
            int friendsCount = 0;
            for (User follower : user.getFollowers()) {
                if (user.getFollowings().contains(follower)) {
                    friendsCount++;
                }
            }

            // Construct UserProfileDto
            UserProfileDto userProfileDto = UserProfileDto.fromUserResponseDto(
                    new UserResponseDto(user.getId(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getGender()),
                    followersCount,
                    followingCount,
                    friendsCount);

            // Return the response using ApiTrait.handleList
            List<UserProfileDto> users = new ArrayList<>();
            users.add(userProfileDto);
            return ApiTrait.handleList(users, "User profile retrieved successfully", HttpStatus.OK);
        } else {
            // Return error response if user not found
            return ApiTrait.errorMessage(new HashMap<>(), "User not found", HttpStatus.NOT_FOUND);
        }
    }

    /*|--------------------------------------------------------------------------
                                        | End Implement
    |-------------------------------------------------------------------------- */




    /*
    |--------------------------------------------------------------------------
    | Implement Unfollow
    |--------------------------------------------------------------------------
    |
    | The Method used to Get Number Of Followers For Everyone
    |
    */
    @Override
    public ResponseEntity<?> getUserFollowers(Integer userId) {
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Set<User> followers = user.getFollowers();

            // Convert followers to a List of UserResponseDto
            List<UserResponseDto> followerDtos = followers.stream()
                    .map(follower -> new UserResponseDto(
                            follower.getId(),
                            follower.getFirstname(),
                            follower.getLastname(),
                            follower.getEmail(),
                            follower.getGender()))
                    .collect(Collectors.toList());

            // Handle the response using ApiTrait
            return ApiTrait.handleUserList(followerDtos, "No followers found");
        } else {
            // Throw exception if user not found
            throw new UserNotFoundException("User not found");
        }
    }

    /*|--------------------------------------------------------------------------
                                        | End Implement
    |-------------------------------------------------------------------------- */




    /*
    |--------------------------------------------------------------------------
    | Implement To Display Number Of Users You are Following
    |--------------------------------------------------------------------------
    |
    | The Method used to unfollow User
    |
    */
    @Override
    public ResponseEntity<?> getUserFollowing(Integer userId) {
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Set<User> following = user.getFollowings();

            // Convert following to a List of UserResponseDto
            List<UserResponseDto> followingDtos = following.stream()
                    .map(followingUser -> new UserResponseDto(
                            followingUser.getId(),
                            followingUser.getFirstname(),
                            followingUser.getLastname(),
                            followingUser.getEmail(),
                            followingUser.getGender()))
                    .collect(Collectors.toList());

            // Handle the response using ApiTrait
            return ApiTrait.handleUserList(followingDtos, "Not following anyone");
        } else {
            // Throw exception if user not found
            throw new UserNotFoundException("User not found");
        }
    }

    /*|--------------------------------------------------------------------------
                                        | End Implement
    |-------------------------------------------------------------------------- */




    /*
    |--------------------------------------------------------------------------
    | Implement Unfollow
    |--------------------------------------------------------------------------
    |
    | The Method used to Get Number of Following for Everyone
    |
    */

    @Override
    public ResponseEntity<?> getUserFriends(Integer userId) {
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Set<User> followers = user.getFollowers(); // Followers of the user
            Set<User> followings = user.getFollowings(); // Users the user is following

            // Find mutual follows (friends)
            Set<User> friends = followers.stream()
                    .filter(followings::contains)
                    .collect(Collectors.toSet());

            // Convert friends to a List of UserResponseDto
            List<UserResponseDto> friendDtos = friends.stream()
                    .map(friend -> new UserResponseDto(
                            friend.getId(),
                            friend.getFirstname(),
                            friend.getLastname(),
                            friend.getEmail(),
                            friend.getGender()))
                    .collect(Collectors.toList());

            // Handle the response using ApiTrait
            return ApiTrait.handleUserList(friendDtos, "No friends found");
        } else {
            // Throw exception if user not found
            throw new UserNotFoundException("User not found");
        }
    }


    /*|--------------------------------------------------------------------------
                                        | End Implement
    |-------------------------------------------------------------------------- */



















}

