package com.pfe.hsnx.repository.search;

import com.pfe.hsnx.domain.Event;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Event} entity.
 */
public interface EventSearchRepository extends ElasticsearchRepository<Event, Long> {
}
