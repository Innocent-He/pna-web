package edu.xidian.pnaWeb.petri.module;

import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @Description
 * @Author He
 * @Date 2022/2/23 22:40
 */
@Data
public class DependInfo implements AlgResult {
	private List<EWSDependInfo> dependInfos;
	@Override
	public String display() {
		StringBuilder sb=new StringBuilder();
		for (EWSDependInfo dependInfo : dependInfos) {
			sb.append("ews:").append(dependInfo.ews).append("\n");
			if (!CollectionUtils.isEmpty(dependInfo.initialSyncDependTrans)) {
				sb.append("syncDependTrans:\n");
				for (DirectDepend initialSyncDependTran : dependInfo.initialSyncDependTrans) {
					sb.append(initialSyncDependTran.directDependTrans).append("\n");
				}
			}
			if (!CollectionUtils.isEmpty(dependInfo.initialSelectDependTrans)) {
				sb.append("selectDependTrans:\n");
				for (DirectDepend initialSelectDependTran : dependInfo.initialSelectDependTrans) {
					sb.append(initialSelectDependTran.directDependTrans).append("\n");
				}
			}

		}
		return sb.toString();
	}


	public static DependBuilder builder() {
		return new DependBuilder();
	}

	public static class DependBuilder{
		private List<EWSDependInfo> dependInfos;
		public void addDependInfo(EWSDependInfo dependInfo) {
			this.dependInfos.add(dependInfo);
		}
		public DependInfo build() {
			DependInfo dependInfo = new DependInfo();
			dependInfo.dependInfos=this.dependInfos;
			return dependInfo;
		}
	}

	@Builder
	static class DirectDepend {
		/**
		 * 直接依赖的变迁集
		 */
		private List<Set<Integer>> directDependTrans;
		/**
		 * 当前ews相关的直接依赖变迁集
		 */
		private Map<Integer, List<Set<Integer>>> dependTransMap;
	}

	@Data
	@NoArgsConstructor
	public static class EWSDependInfo {
		/**
		 * 直接的同步依赖变迁集
		 */
		private List<DirectDepend> initialSyncDependTrans;
		/**
		 * 直接的选择依赖变迁集
		 */
		private List<DirectDepend> initialSelectDependTrans;
		/**
		 * 当前ews
		 */
		private List<Integer> ews;


		public static Builder builder() {
			return new Builder();
		}

		public static class Builder {
			/**
			 * 直接的同步依赖变迁集
			 */
			private List<DirectDepend> initialSyncDependTrans;
			/**
			 * 直接的选择依赖变迁集
			 */
			private List<DirectDepend> initialSelectDependTrans;
			/**
			 * 当前ews
			 */
			private List<Integer> ews;

			public Builder addSyncDependTrans(List<Set<Integer>> trans, Map<Integer, List<Set<Integer>>> dependTransMap) {
				DirectDepend directDepend = DirectDepend.builder()
						.directDependTrans(trans)
						.dependTransMap(dependTransMap)
						.build();
				if (Objects.isNull(this.initialSyncDependTrans)) {
					this.initialSyncDependTrans= Lists.newArrayList();
				}
				this.initialSyncDependTrans.add(directDepend);
				return this;
			}

			public Builder addSelectDependTrans(List<Set<Integer>> trans,Map<Integer, List<Set<Integer>>> dependTransMap) {
				DirectDepend directDepend = DirectDepend.builder()
						.directDependTrans(trans)
						.dependTransMap(dependTransMap)
						.build();
				if (Objects.isNull(this.initialSelectDependTrans)) {
					this.initialSelectDependTrans= Lists.newArrayList();
				}
				this.initialSelectDependTrans.add(directDepend);
				return this;
			}

			public Builder ews(List<Integer> ews) {
				this.ews = ews;
				return this;
			}
			public EWSDependInfo build() {
				EWSDependInfo dependTransInfo = new EWSDependInfo();
				dependTransInfo.setInitialSelectDependTrans(initialSelectDependTrans);
				dependTransInfo.setInitialSyncDependTrans(initialSyncDependTrans);
				dependTransInfo.setEws(ews);
				return dependTransInfo;
			}
		}
	}

}
