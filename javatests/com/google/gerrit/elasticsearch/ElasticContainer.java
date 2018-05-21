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
DECL|field|NAME
specifier|private
specifier|static
specifier|final
name|String
name|NAME
init|=
literal|"elasticsearch"
decl_stmt|;
DECL|field|VERSION
specifier|private
specifier|static
specifier|final
name|String
name|VERSION
init|=
literal|"2.4.6-alpine"
decl_stmt|;
DECL|field|ELASTICSEARCH_DEFAULT_PORT
specifier|private
specifier|static
specifier|final
name|int
name|ELASTICSEARCH_DEFAULT_PORT
init|=
literal|9200
decl_stmt|;
DECL|method|ElasticContainer ()
specifier|public
name|ElasticContainer
parameter_list|()
block|{
name|this
argument_list|(
name|NAME
operator|+
literal|":"
operator|+
name|VERSION
argument_list|)
expr_stmt|;
block|}
DECL|method|ElasticContainer (String dockerImageName)
specifier|public
name|ElasticContainer
parameter_list|(
name|String
name|dockerImageName
parameter_list|)
block|{
name|super
argument_list|(
name|dockerImageName
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
DECL|method|getLivenessCheckPorts ()
specifier|protected
name|Set
argument_list|<
name|Integer
argument_list|>
name|getLivenessCheckPorts
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

