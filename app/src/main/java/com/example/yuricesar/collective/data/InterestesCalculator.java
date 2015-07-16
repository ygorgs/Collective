package com.example.yuricesar.collective.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsavel por calcular o nivel de coisas em comum que um usuário tem com outro.
 * 
 * @author ygorg_000
 *
 */
public class InterestesCalculator {
	
	public List<String> itensEmComum;
	public List<String> todosOsItens;
	
	/**
	 * Construtor
	 */
	public InterestesCalculator(){
		itensEmComum = new ArrayList<String>();
		todosOsItens = new ArrayList<String>();
	}
	
	/**
	 * Calcula a porcentagem de coisas que o user2 tem com o user1 (o calculo eh feito em relação aos itens do user1).
	 * @param user1 Usuario 1.
	 * @param user2 Usuario 2.
	 * @param categorys Quais categorias devem ser consideradas no calculo.
	 * @return 
	 */
	public double getLevel(UserInfo user1, UserInfo user2, List<Category> categorys){
		List<String> c1;
		List<String> c2;
		for (Category category : categorys) {
			c1 = getUserInterests(user1, category);
			c2 = getUserInterests(user2, category);	
			calcular(c1, c2);
		}
		if(todosOsItens.size() > 0){
			return Double.parseDouble(Integer.toString(itensEmComum.size()))/todosOsItens.size();
		}else{
			return 0.0;
		}
	}

    /**
     * Calcula
     * @param c1
     * @param c2
     */
	private void calcular(List<String> c1, List<String> c2) {
		todosOsItens.addAll(c1);
		for (String item : c2) {
			if(todosOsItens.contains(item)){
				itensEmComum.add(item);
			}
		}
	}

	/**
	 * Retorna a lista de coisas que o usuario curti de acordo com a categoria
	 * @param user
	 * @param category
	 * @return
	 */
	private List<String> getUserInterests(UserInfo user, Category category) {
		switch(category){
			case BOOKS:
				return user.getBooks();
			case GAMES:
				return user.getGames();
			case MOVIES:
				return user.getMovies();
			case MUSIC:
				return user.getMusic();
			case TV:
				return user.getTv();
		}
		return null;
	}

}
