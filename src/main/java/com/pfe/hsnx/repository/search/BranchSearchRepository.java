package com.pfe.hsnx.repository.search;

import com.pfe.hsnx.domain.Branch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Branch} entity.
 */
public interface BranchSearchRepository extends ElasticsearchRepository<Branch, Long> {
}
