package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RewardCalculator {
	public static final int DEFAULT_COUNT = 0;

	private final Map<Rank, Integer> rankMatchingCounts;

	public RewardCalculator() {
		this.rankMatchingCounts = new HashMap<>();
	}

	public void addCount(Rank rank) {
		int rankCount = rankMatchingCounts.getOrDefault(rank, DEFAULT_COUNT);
		rankMatchingCounts.put(rank, rankCount + 1);
	}

	public int getCount(Rank rank) {
		return rankMatchingCounts.getOrDefault(rank, DEFAULT_COUNT);
	}

	public int sum() {
		int sum = 0;
		for (Rank rank : rankMatchingCounts.keySet()) {
			sum += rankMatchingCounts.get(rank) * rank.getReward();
		}
		return sum;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		RewardCalculator that = (RewardCalculator)o;
		return Objects.equals(rankMatchingCounts, that.rankMatchingCounts);
	}

	@Override
	public int hashCode() {
		return Objects.hash(rankMatchingCounts);
	}
}
