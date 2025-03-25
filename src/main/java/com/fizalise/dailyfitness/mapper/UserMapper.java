package com.fizalise.dailyfitness.mapper;

import com.fizalise.dailyfitness.dto.authentication.UserRequest;
import com.fizalise.dailyfitness.dto.authentication.UserResponse;
import com.fizalise.dailyfitness.entity.Gender;
import com.fizalise.dailyfitness.entity.Role;
import com.fizalise.dailyfitness.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Mapper(componentModel = "spring", imports = Long.class)
public abstract class UserMapper {
    @Autowired
    protected PasswordEncoder passwordEncoder;
    @Mapping(target = "password", qualifiedByName = "getEncodedPassword")
    @Mapping(source = "requestUser", target = "dailyNorm", qualifiedByName = "getCalculatedDailyNorm")
    public abstract User toUser(UserRequest requestUser, Role role);
    public abstract UserResponse toResponse(User user);
    public abstract List<UserResponse> toResponses(Page<User> users);
    @Named("getEncodedPassword")
    protected String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
    @Named("getCalculatedDailyNorm")
    protected int harrisBenedict(UserRequest requestUser) {
        double weight = requestUser.weight().doubleValue();
        double growth = requestUser.growth().doubleValue();
        double age = requestUser.age();
        double bmr = (requestUser.gender() == Gender.MALE) ?
                88.36 + (13.4 * weight) + (4.8 * growth) - (5.7 * age) :
                447.6 + (9.2 * weight) + (3.1 * growth) - (4.3 * age);
        return switch (requestUser.activity()) {
            case NONE ->  ((int) (bmr * 1.2));
            case MIN -> ((int) (bmr * 1.375));
            case MIDDLE -> ((int) (bmr * 1.55));
            case HIGH -> ((int) (bmr * 1.725));
            case EXTREME -> ((int) (bmr * 1.9));
        };
    }
}
