package learn.scoreshelf.data;

import learn.scoreshelf.models.Game;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GameRepository {
    List<Game> findAll();

    Game findById(int gameId);

    Game add(Game game);

    boolean update(Game game);

    @Transactional
    boolean deleteById(int gameId);
}
