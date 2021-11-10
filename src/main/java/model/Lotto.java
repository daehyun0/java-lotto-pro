package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import calculator.StringSplitParser;

public class Lotto {
	public static final String MESSAGE_NOT_ALLOW_LENGTH = "LOTTO_NUMBERS_LENGTH_MUST_BE_6";
	public static final String MESSAGE_NOT_ALLOW_DUPLICATION = "LOTTO_NUMBERS_ELEMENT_MUST_BE_INDEPENDENT";
	public static final int LOTTO_NUMBERS_LENGTH = 6;

	private final List<LottoNumber> lottoNumbers;

	public Lotto(List<LottoNumber> lottoNumbers) {
		if (lottoNumbers.size() != LOTTO_NUMBERS_LENGTH) {
			throw new IllegalArgumentException(MESSAGE_NOT_ALLOW_LENGTH);
		}
		validateDuplication(lottoNumbers);

		this.lottoNumbers = new ArrayList<>(lottoNumbers);
		sortLottoNumbers(lottoNumbers);
	}

	public Lotto(LottoNumberChoiceStrategy lottoNumberChoiceStrategy) {
		this(lottoNumberChoiceStrategy.choose());
	}

	public Lotto(String lottoNumbersString) {
		this(parse(lottoNumbersString));
	}

	public Rank calcLottoResult(Lotto winningLotto) {
		LottoResult lottoResult = new LottoResult();
		for (LottoNumber winningLottoNumber : winningLotto.lottoNumbers) {
			containWinningLottoNumber(winningLottoNumber, lottoResult);
		}
		return lottoResult.getRank();
	}

	private void containWinningLottoNumber(LottoNumber winningLottoNumber, LottoResult lottoResult) {
		if (lottoNumbers.contains(winningLottoNumber)) {
			lottoResult.addMatchingCount();
		}
	}

	private static List<LottoNumber> parse(String lottoNumbersString) {
		return StringSplitParser.parse(lottoNumbersString)
			.stream()
			.map(LottoNumber::new)
			.collect(Collectors.toList());
	}

	private void sortLottoNumbers(List<LottoNumber> lottoNumbers) {
		lottoNumbers.sort(LottoNumber::getComparatorOther);
	}

	private void validateDuplication(List<LottoNumber> lottoNumbers) {
		Set<LottoNumber> lottoNumbersWithoutDuplication = new HashSet<>(lottoNumbers);
		if (lottoNumbersWithoutDuplication.size() != lottoNumbers.size()) {
			throw new IllegalArgumentException(MESSAGE_NOT_ALLOW_DUPLICATION);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Lotto that = (Lotto)o;
		return Objects.equals(lottoNumbers, that.lottoNumbers);
	}

	@Override
	public int hashCode() {
		return Objects.hash(lottoNumbers);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(lottoNumbers
			.stream()
			.map(LottoNumber::toString)
			.collect(Collectors.joining(", "))
		);
		sb.append("]");
		return sb.toString();
	}
}
