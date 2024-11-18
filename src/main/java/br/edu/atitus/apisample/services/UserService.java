package br.edu.atitus.apisample.services;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import br.edu.atitus.apisample.entities.UserEntity;
import br.edu.atitus.apisample.repositories.UserRepository;

@Service
public class UserService {
	//Essa classe possui uma dependência de um objeto UserRepository
	private final UserRepository repository;
	//No método construtor existe a injeção de dependência
	public UserService(UserRepository repository) {
		super();
		this.repository = repository;
	}

	public static class EmailValidator {

		private static final String EMAIL_REGEX = "^[\\w!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
		private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

		public static boolean isValidEmail(String email) {
			if (email == null) {
				return false;
			}
			Matcher matcher = EMAIL_PATTERN.matcher(email);
			return matcher.matches();
		}
	}

	public UserEntity save(UserEntity newUser) throws Exception {
		// TODO validar regras de negócio
		if (newUser == null)
			throw new Exception("Objeto nulo!");

		if (newUser.getName() == null || newUser.getName().isEmpty())
			throw new Exception("Nome inválido!");
		newUser.setName(newUser.getName().trim());

		if (newUser.getEmail() == null || newUser.getEmail().isEmpty())
			throw new Exception("Email inválido!");

		// validar o email com regex
		if (!EmailValidator.isValidEmail(newUser.getEmail()))
			throw new Exception("Email inválido!");

		newUser.setEmail(newUser.getEmail().trim());
		
		if (repository.existsByEmail(newUser.getEmail()))
			throw new Exception("Já existe usuário com este e-mail!");

		// TODO invocar método camada repository
		this.repository.save(newUser);
		
		
		return newUser;
	}
	
	public List<UserEntity> findAll() throws Exception {
		return repository.findAll();
	}
}
