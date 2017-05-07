/*
 * Copyright 2017 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.netflix.spinnaker.halyard.deploy.spinnaker.v1.service.bake.debian;

import com.netflix.spinnaker.halyard.config.model.v1.node.DeploymentConfiguration;
import com.netflix.spinnaker.halyard.deploy.services.v1.ArtifactService;
import com.netflix.spinnaker.halyard.deploy.spinnaker.v1.SpinnakerRuntimeSettings;
import com.netflix.spinnaker.halyard.deploy.spinnaker.v1.profile.Profile;
import com.netflix.spinnaker.halyard.deploy.spinnaker.v1.service.DeckService;
import com.netflix.spinnaker.halyard.deploy.spinnaker.v1.service.ServiceSettings;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Component
public class BakeDebianDeckService extends DeckService implements BakeDebianService<DeckService.Deck> {
  @Autowired
  ArtifactService artifactService;

  StartupPriority priority = new StartupPriority(StartupPriority.LOW);

  final String upstartServiceName = "apache2";

  @Override
  public String getStartupCommand() {
    return String.join("\n", "a2ensite spinnaker", "service apache2 restart");
  }

  @Override
  public List<Profile> getProfiles(DeploymentConfiguration deploymentConfiguration, SpinnakerRuntimeSettings endpoints) {
    return new ArrayList<>();
  }

  @Override
  public ServiceSettings buildServiceSettings(DeploymentConfiguration deploymentConfiguration) {
    return new Settings(deploymentConfiguration.getSecurity().getUiSecurity())
        .setArtifactId(getArtifactId(deploymentConfiguration.getName()))
        .setEnabled(true);
  }

  public String getArtifactId(String deploymentName) {
    return BakeDebianService.super.getArtifactId(deploymentName);
  }
}