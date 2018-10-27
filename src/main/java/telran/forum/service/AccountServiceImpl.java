package telran.forum.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telran.forum.configuration.AccountConfiguration;
import telran.forum.dao.UserAccountRepository;
import telran.forum.domain.UserAccount;
import telran.forum.dto.UserProfileDto;
import telran.forum.dto.UserRegisterDto;

@Service
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	UserAccountRepository userRepository;
	
	@Autowired
	AccountConfiguration accountConfiguration;

	@Override
	public UserProfileDto addUser(UserRegisterDto userRegDto) {
		if (userRepository.existsById(userRegDto.getId())) {
			throw new UserExistException();
		}
		String hashPassword = accountConfiguration
				.getEncodePassword(userRegDto.getPassword());
		
		UserAccount userAccount = UserAccount.builder()
				.id(userRegDto.getId())
				.password(hashPassword)
				.firstName(userRegDto.getFirstName())
				.lastName(userRegDto.getLastName())
				.role("User")
				.expDate(LocalDateTime.now().plusDays(accountConfiguration.getExpPeriod()))
				.build();
		userRepository.save(userAccount);
		return new UserProfileDto(userRegDto.getId(),
				userRegDto.getFirstName(),userRegDto.getLastName());
	}


	@Override
	public UserProfileDto editUser(UserRegisterDto userRegDto) {
		UserAccount userByIdFromRepo = userRepository
				.findById(userRegDto.getId()).orElse(null);
		userByIdFromRepo.setFirstName(userRegDto.getFirstName());
		userByIdFromRepo.setLastName(userRegDto.getLastName());
		userRepository.save(userByIdFromRepo);
		return new UserProfileDto(userRegDto.getId(), userRegDto.getFirstName(),
				userRegDto.getLastName());
	}

	@Override
	public UserProfileDto removeUser(String id) {
		UserAccount user = 	userRepository.findById(id).orElse(null);
		if (user == null) {
			return null;
		}
		userRepository.delete(user);
		return new UserProfileDto(
			user.getId(), 
			user.getFirstName(),
			user.getLastName()
		);
	}

	@Override
	public Set<String> addRole(String id, String role) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> removeRole(String id, String role) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changePassword(String password) {
		// TODO Auto-generated method stub

	}

}
