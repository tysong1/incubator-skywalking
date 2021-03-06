/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.skywalking.apm.collector.storage.es.dao.srmp;

import java.util.HashMap;
import java.util.Map;
import org.apache.skywalking.apm.collector.client.elasticsearch.ElasticSearchClient;
import org.apache.skywalking.apm.collector.core.annotations.trace.GraphComputingMetric;
import org.apache.skywalking.apm.collector.storage.es.MetricTransformUtil;
import org.apache.skywalking.apm.collector.storage.es.base.dao.AbstractPersistenceEsDAO;
import org.apache.skywalking.apm.collector.storage.table.service.ServiceReferenceMetric;
import org.apache.skywalking.apm.collector.storage.table.service.ServiceReferenceMetricTable;

/**
 * @author peng-yongsheng
 */
public abstract class AbstractServiceReferenceMetricEsPersistenceDAO extends AbstractPersistenceEsDAO<ServiceReferenceMetric> {

    AbstractServiceReferenceMetricEsPersistenceDAO(ElasticSearchClient client) {
        super(client);
    }

    @Override protected final String timeBucketColumnNameForDelete() {
        return ServiceReferenceMetricTable.TIME_BUCKET.getName();
    }

    @Override protected final ServiceReferenceMetric esDataToStreamData(Map<String, Object> source) {
        ServiceReferenceMetric serviceReferenceMetric = new ServiceReferenceMetric();
        serviceReferenceMetric.setMetricId((String)source.get(ServiceReferenceMetricTable.METRIC_ID.getName()));

        serviceReferenceMetric.setFrontApplicationId(((Number)source.get(ServiceReferenceMetricTable.FRONT_APPLICATION_ID.getName())).intValue());
        serviceReferenceMetric.setBehindApplicationId(((Number)source.get(ServiceReferenceMetricTable.BEHIND_APPLICATION_ID.getName())).intValue());
        serviceReferenceMetric.setFrontInstanceId(((Number)source.get(ServiceReferenceMetricTable.FRONT_INSTANCE_ID.getName())).intValue());
        serviceReferenceMetric.setBehindInstanceId(((Number)source.get(ServiceReferenceMetricTable.BEHIND_INSTANCE_ID.getName())).intValue());
        serviceReferenceMetric.setFrontServiceId(((Number)source.get(ServiceReferenceMetricTable.FRONT_SERVICE_ID.getName())).intValue());
        serviceReferenceMetric.setBehindServiceId(((Number)source.get(ServiceReferenceMetricTable.BEHIND_SERVICE_ID.getName())).intValue());

        MetricTransformUtil.INSTANCE.esDataToStreamData(source, serviceReferenceMetric);

        return serviceReferenceMetric;
    }

    @Override protected final Map<String, Object> esStreamDataToEsData(ServiceReferenceMetric streamData) {
        Map<String, Object> target = new HashMap<>();
        target.put(ServiceReferenceMetricTable.METRIC_ID.getName(), streamData.getMetricId());

        target.put(ServiceReferenceMetricTable.FRONT_APPLICATION_ID.getName(), streamData.getFrontApplicationId());
        target.put(ServiceReferenceMetricTable.BEHIND_APPLICATION_ID.getName(), streamData.getBehindApplicationId());
        target.put(ServiceReferenceMetricTable.FRONT_INSTANCE_ID.getName(), streamData.getFrontInstanceId());
        target.put(ServiceReferenceMetricTable.BEHIND_INSTANCE_ID.getName(), streamData.getBehindInstanceId());
        target.put(ServiceReferenceMetricTable.FRONT_SERVICE_ID.getName(), streamData.getFrontServiceId());
        target.put(ServiceReferenceMetricTable.BEHIND_SERVICE_ID.getName(), streamData.getBehindServiceId());

        MetricTransformUtil.INSTANCE.esStreamDataToEsData(streamData, target);

        return target;
    }

    @GraphComputingMetric(name = "/persistence/get/" + ServiceReferenceMetricTable.TABLE)
    @Override public final ServiceReferenceMetric get(String id) {
        return super.get(id);
    }
}
