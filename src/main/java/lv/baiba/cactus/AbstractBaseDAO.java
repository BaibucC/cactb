package lv.baiba.cactus;
import java.util.List;

public interface AbstractBaseDAO<T> {
	void delete(T obj);

	T find(Integer id);

	void save(T t);

	void update(T t);

	List<T> findAll();

	Integer countAll();
}
