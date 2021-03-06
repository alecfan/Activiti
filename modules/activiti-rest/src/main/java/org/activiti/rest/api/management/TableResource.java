/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.rest.api.management;

import java.util.Map;
import java.util.Map.Entry;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.rest.api.ActivitiUtil;
import org.activiti.rest.api.SecuredResource;
import org.activiti.rest.application.ActivitiRestServicesApplication;
import org.restlet.resource.Get;

/**
 * @author Frederik Heremans
 */
public class TableResource extends SecuredResource {
  
  @Get
  public TableResponse getTable() {
    if(authenticate() == false) return null;

    String tableName = getAttribute("tableName");
    if(tableName == null) {
      throw new ActivitiIllegalArgumentException("The tableName cannot be null");
    }
    
   Map<String, Long> tableCounts = ActivitiUtil.getManagementService().getTableCount();

   TableResponse response = null;
   for(Entry<String, Long> entry : tableCounts.entrySet()) {
     if(entry.getKey().equals(tableName)) {
       response = getApplication(ActivitiRestServicesApplication.class).getRestResponseFactory()
               .createTableResponse(this, entry.getKey(), entry.getValue());
       break;
     }
   }
   
   if(response == null) {
     throw new ActivitiObjectNotFoundException("Could not find a table with name '" + tableName + "'.", String.class);
   }
   return response;
  }
}
