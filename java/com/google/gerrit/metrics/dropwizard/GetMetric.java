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
name|Response
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
DECL|class|GetMetric
class|class
name|GetMetric
implements|implements
name|RestReadView
argument_list|<
name|MetricResource
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
name|Inject
DECL|method|GetMetric (PermissionBackend permissionBackend, DropWizardMetricMaker metrics)
name|GetMetric
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
DECL|method|apply (MetricResource resource)
specifier|public
name|Response
argument_list|<
name|MetricJson
argument_list|>
name|apply
parameter_list|(
name|MetricResource
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
return|return
name|Response
operator|.
name|ok
argument_list|(
operator|new
name|MetricJson
argument_list|(
name|resource
operator|.
name|getMetric
argument_list|()
argument_list|,
name|metrics
operator|.
name|getAnnotations
argument_list|(
name|resource
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|,
name|dataOnly
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

