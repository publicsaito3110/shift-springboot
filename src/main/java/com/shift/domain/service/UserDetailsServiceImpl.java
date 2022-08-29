package com.shift.domain.service;

import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.shift.domain.model.entity.UserEntity;
import com.shift.domain.repository.UserRepository;

/**
 * @author saito
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		//usernameを検索
		Optional<UserEntity> userEntityOpt = userRepository.findById(username);

		//usernameが存在しないとき
		if (!userEntityOpt.isPresent()) {
			throw new UsernameNotFoundException(username + " is not found");
		}

		//---------------------
		// UserDetailsを返却
		//---------------------

		//UserEntityで取得
		UserEntity userEntity = userEntityOpt.get();

		//User(UserDetailsインターフェースの実装クラス)にセットして返却
		var grantedAuthorities = new HashSet<GrantedAuthority>();
		grantedAuthorities.add(new SimpleGrantedAuthority(userEntity.getAdminFlgFormatRole()));

		return new User(userEntity.getId(), userEntity.getPassword(), grantedAuthorities);
	}
}
