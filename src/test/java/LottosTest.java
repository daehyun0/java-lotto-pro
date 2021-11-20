import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import model.Lotto;
import model.LottoNumber;
import model.Lottos;
import model.Rank;
import model.RewardCalculator;

public class LottosTest {
	@Test
	@DisplayName("로또 숫자가 6개가 주어지지 않으면 예외")
	void test_constructor1() {
		assertThatThrownBy(() ->
			new Lottos(
				Collections.singletonList(
					Arrays.asList(1, 2)
				)
			)).isInstanceOf(IllegalArgumentException.class)
			.hasMessage(Lotto.MESSAGE_NOT_ALLOW_LENGTH);
	}

	@Test
	@DisplayName("로또 숫자에 중복된 값이 포함되어있으면 예외")
	void test_constructor2() {
		assertThatThrownBy(() ->
			new Lottos(
				Collections.singletonList(
					Arrays.asList(1, 1, 3, 4, 5, 7)
				)
			)).isInstanceOf(IllegalArgumentException.class)
			.hasMessage(Lotto.MESSAGE_NOT_ALLOW_DUPLICATION);
	}

	@Test
	@DisplayName("로또 숫자가 6개가 포함되면서 겹치는 값이 없으면 성공")
	void test_constructor3() {
		assertThatNoException()
			.isThrownBy(() -> {
				new Lottos(
					Collections.singletonList(
						Arrays.asList(1, 2, 3, 4, 5, 6)
					)
				);
			});
	}

	@Test
	@DisplayName("List<List<Integer>>를 이용해 Lottos 생성")
	void test_constructor4() {
		List<List<Integer>> nestedLottoNumbers = new ArrayList<>();
		List<Integer> lottoNumbers1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
		List<Integer> lottoNumbers2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 7));
		nestedLottoNumbers.add(lottoNumbers1);
		nestedLottoNumbers.add(lottoNumbers2);

		assertThat(new Lottos(nestedLottoNumbers)).isEqualTo(new Lottos(nestedLottoNumbers));
	}

	@ParameterizedTest
	@DisplayName("당첨 번호를 제공하면 해당하는 RewardCalculator 반환")
	@MethodSource("test_calcReward1_parameter")
	void test_calcReward1(List<Integer> userLotto, List<Integer> winningLotto, Rank expectedRank, int bonusNumber) {
		Lottos lottos = new Lottos(Collections.singletonList(userLotto));
		RewardCalculator rewardCalculator = lottos.calcReward(new Lotto(winningLotto), new LottoNumber(bonusNumber));
		RewardCalculator expectedRewardCalculator = new RewardCalculator();
		expectedRewardCalculator.addCount(expectedRank);

		assertThat(rewardCalculator).isEqualTo(expectedRewardCalculator);
	}

	private static Stream<Arguments> test_calcReward1_parameter() {
		int bonusNumberHit = 6;
		int bonusNumberNotHit = 45;

		return Stream.of(
			Arguments.of(Arrays.asList(1, 2, 3, 4, 5, 6), Arrays.asList(1, 2, 3, 4, 5, 6), Rank.FIRST,
				bonusNumberNotHit),
			Arguments.of(Arrays.asList(1, 2, 3, 4, 5, 6), Arrays.asList(1, 2, 3, 4, 5, 7), Rank.SECOND, bonusNumberHit),
			Arguments.of(Arrays.asList(1, 2, 3, 4, 5, 6), Arrays.asList(1, 2, 3, 4, 5, 7), Rank.THIRD,
				bonusNumberNotHit),
			Arguments.of(Arrays.asList(1, 2, 3, 4, 5, 6), Arrays.asList(1, 2, 3, 4, 7, 8), Rank.FOURTH, bonusNumberHit),
			Arguments.of(Arrays.asList(1, 2, 3, 4, 5, 6), Arrays.asList(1, 2, 3, 7, 8, 9), Rank.FIFTH, bonusNumberHit),
			Arguments.of(Arrays.asList(1, 2, 3, 4, 5, 6), Arrays.asList(1, 2, 7, 8, 9, 10), Rank.NONE, bonusNumberHit),
			Arguments.of(Arrays.asList(1, 2, 3, 4, 5, 6), Arrays.asList(1, 7, 8, 9, 10, 11), Rank.NONE, bonusNumberHit),
			Arguments.of(Arrays.asList(1, 2, 3, 4, 5, 6), Arrays.asList(7, 8, 9, 10, 11, 12), Rank.NONE, bonusNumberHit)
		);
	}

	@Test
	@DisplayName("원하는 형태의 문자열이 반환되는지 확인")
	void test_toString() {
		Lottos lottos = new Lottos(
			Arrays.asList(
				Arrays.asList(1, 2, 3, 4, 5, 6),
				Arrays.asList(1, 2, 3, 4, 5, 7)
			)
		);

		assertThat(lottos.toString())
			.matches("(\\[\\d+, \\d+, \\d+, \\d+, \\d+, \\d+]\\n?)+");
	}
}
