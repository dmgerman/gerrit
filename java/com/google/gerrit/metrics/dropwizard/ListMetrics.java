begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.metrics.dropwizard
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|metrics
operator|.
name|dropwizard
package|;
end_package

begin_import
import|import
name|com
operator|.
name|codahale
operator|.
name|metrics
operator|.
name|Metric
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|restapi
operator|.
name|AuthException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|restapi
operator|.
name|RestReadView
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|ConfigResource
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|permissions
operator|.
name|GlobalPermission
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|permissions
operator|.
name|PermissionBackend
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|permissions
operator|.
name|PermissionBackendException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|Option
import|;
end_import

begin_class
DECL|class|ListMetrics
class|class
name|ListMetrics
implements|implements
name|RestReadView
argument_list|<
name|ConfigResource
argument_list|>
block|{
DECL|field|permissionBackend
specifier|private
specifier|final
name|PermissionBackend
name|permissionBackend
decl_stmt|;
DECL|field|metrics
specifier|private
specifier|final
name|DropWizardMetricMaker
name|metrics
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--data-only"
argument_list|,
name|usage
operator|=
literal|"return only values"
argument_list|)
DECL|field|dataOnly
name|boolean
name|dataOnly
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--prefix"
argument_list|,
name|aliases
operator|=
block|{
literal|"-p"
block|}
argument_list|,
name|metaVar
operator|=
literal|"PREFIX"
argument_list|,
name|usage
operator|=
literal|"match metric by exact match or prefix"
argument_list|)
DECL|field|query
name|List
argument_list|<
name|String
argument_list|>
name|query
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Inject
DECL|method|ListMetrics (PermissionBackend permissionBackend, DropWizardMetricMaker metrics)
name|ListMetrics
parameter_list|(
name|PermissionBackend
name|permissionBackend
parameter_list|,
name|DropWizardMetricMaker
name|metrics
parameter_list|)
block|{
name|this
operator|.
name|permissionBackend
operator|=
name|permissionBackend
expr_stmt|;
name|this
operator|.
name|metrics
operator|=
name|metrics
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ConfigResource resource)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|MetricJson
argument_list|>
name|apply
parameter_list|(
name|ConfigResource
name|resource
parameter_list|)
throws|throws
name|AuthException
throws|,
name|PermissionBackendException
block|{
name|permissionBackend
operator|.
name|currentUser
argument_list|()
operator|.
name|check
argument_list|(
name|GlobalPermission
operator|.
name|VIEW_CACHES
argument_list|)
expr_stmt|;
name|SortedMap
argument_list|<
name|String
argument_list|,
name|MetricJson
argument_list|>
name|out
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|prefixes
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|query
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|q
range|:
name|query
control|)
block|{
if|if
condition|(
name|q
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|prefixes
operator|.
name|add
argument_list|(
name|q
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Metric
name|m
init|=
name|metrics
operator|.
name|getMetric
argument_list|(
name|q
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|put
argument_list|(
name|q
argument_list|,
name|toJson
argument_list|(
name|q
argument_list|,
name|m
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|query
operator|.
name|isEmpty
argument_list|()
operator|||
operator|!
name|prefixes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|String
name|name
range|:
name|metrics
operator|.
name|getMetricNames
argument_list|()
control|)
block|{
if|if
condition|(
name|include
argument_list|(
name|prefixes
argument_list|,
name|name
argument_list|)
condition|)
block|{
name|out
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|toJson
argument_list|(
name|name
argument_list|,
name|metrics
operator|.
name|getMetric
argument_list|(
name|name
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|out
return|;
block|}
DECL|method|toJson (String q, Metric m)
specifier|private
name|MetricJson
name|toJson
parameter_list|(
name|String
name|q
parameter_list|,
name|Metric
name|m
parameter_list|)
block|{
return|return
operator|new
name|MetricJson
argument_list|(
name|m
argument_list|,
name|metrics
operator|.
name|getAnnotations
argument_list|(
name|q
argument_list|)
argument_list|,
name|dataOnly
argument_list|)
return|;
block|}
DECL|method|include (List<String> prefixes, String name)
specifier|private
specifier|static
name|boolean
name|include
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|prefixes
parameter_list|,
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|prefixes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
for|for
control|(
name|String
name|p
range|:
name|prefixes
control|)
block|{
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
name|p
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

