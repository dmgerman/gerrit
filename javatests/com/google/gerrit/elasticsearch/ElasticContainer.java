begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.elasticsearch
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|elasticsearch
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpHost
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AssumptionViolatedException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|testcontainers
operator|.
name|containers
operator|.
name|GenericContainer
import|;
end_import

begin_comment
comment|/* Helper class for running ES integration tests in docker container */
end_comment

begin_class
DECL|class|ElasticContainer
specifier|public
class|class
name|ElasticContainer
parameter_list|<
name|SELF
extends|extends
name|ElasticContainer
parameter_list|<
name|SELF
parameter_list|>
parameter_list|>
extends|extends
name|GenericContainer
argument_list|<
name|SELF
argument_list|>
block|{
DECL|field|ELASTICSEARCH_DEFAULT_PORT
specifier|private
specifier|static
specifier|final
name|int
name|ELASTICSEARCH_DEFAULT_PORT
init|=
literal|9200
decl_stmt|;
DECL|method|createAndStart (ElasticVersion version)
specifier|public
specifier|static
name|ElasticContainer
argument_list|<
name|?
argument_list|>
name|createAndStart
parameter_list|(
name|ElasticVersion
name|version
parameter_list|)
block|{
comment|// Assumption violation is not natively supported by Testcontainers.
comment|// See https://github.com/testcontainers/testcontainers-java/issues/343
try|try
block|{
name|ElasticContainer
argument_list|<
name|?
argument_list|>
name|container
init|=
operator|new
name|ElasticContainer
argument_list|<>
argument_list|(
name|version
argument_list|)
decl_stmt|;
name|container
operator|.
name|start
argument_list|()
expr_stmt|;
return|return
name|container
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|AssumptionViolatedException
argument_list|(
literal|"Unable to start container"
argument_list|,
name|t
argument_list|)
throw|;
block|}
block|}
DECL|method|createAndStart ()
specifier|public
specifier|static
name|ElasticContainer
argument_list|<
name|?
argument_list|>
name|createAndStart
parameter_list|()
block|{
return|return
name|createAndStart
argument_list|(
name|ElasticVersion
operator|.
name|V2_4
argument_list|)
return|;
block|}
DECL|method|getImageName (ElasticVersion version)
specifier|private
specifier|static
name|String
name|getImageName
parameter_list|(
name|ElasticVersion
name|version
parameter_list|)
block|{
switch|switch
condition|(
name|version
condition|)
block|{
case|case
name|V2_4
case|:
return|return
literal|"elasticsearch:2.4.6-alpine"
return|;
case|case
name|V5_6
case|:
return|return
literal|"docker.elastic.co/elasticsearch/elasticsearch:5.6.13"
return|;
case|case
name|V6_2
case|:
return|return
literal|"docker.elastic.co/elasticsearch/elasticsearch-oss:6.2.4"
return|;
case|case
name|V6_3
case|:
return|return
literal|"docker.elastic.co/elasticsearch/elasticsearch-oss:6.3.2"
return|;
case|case
name|V6_4
case|:
return|return
literal|"docker.elastic.co/elasticsearch/elasticsearch-oss:6.4.3"
return|;
case|case
name|V6_5
case|:
return|return
literal|"docker.elastic.co/elasticsearch/elasticsearch-oss:6.5.0"
return|;
block|}
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No tests for version: "
operator|+
name|version
operator|.
name|name
argument_list|()
argument_list|)
throw|;
block|}
DECL|method|ElasticContainer (ElasticVersion version)
specifier|private
name|ElasticContainer
parameter_list|(
name|ElasticVersion
name|version
parameter_list|)
block|{
name|super
argument_list|(
name|getImageName
argument_list|(
name|version
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|addExposedPort
argument_list|(
name|ELASTICSEARCH_DEFAULT_PORT
argument_list|)
expr_stmt|;
comment|// https://github.com/docker-library/elasticsearch/issues/58
name|addEnv
argument_list|(
literal|"-Ees.network.host"
argument_list|,
literal|"0.0.0.0"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getLivenessCheckPortNumbers ()
specifier|public
name|Set
argument_list|<
name|Integer
argument_list|>
name|getLivenessCheckPortNumbers
parameter_list|()
block|{
return|return
name|ImmutableSet
operator|.
name|of
argument_list|(
name|getMappedPort
argument_list|(
name|ELASTICSEARCH_DEFAULT_PORT
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getHttpHost ()
specifier|public
name|HttpHost
name|getHttpHost
parameter_list|()
block|{
return|return
operator|new
name|HttpHost
argument_list|(
name|getContainerIpAddress
argument_list|()
argument_list|,
name|getMappedPort
argument_list|(
name|ELASTICSEARCH_DEFAULT_PORT
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

