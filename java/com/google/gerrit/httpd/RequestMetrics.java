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
DECL|package|com.google.gerrit.httpd
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
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
name|metrics
operator|.
name|Counter1
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
name|metrics
operator|.
name|Description
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
name|metrics
operator|.
name|Field
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
name|metrics
operator|.
name|MetricMaker
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|RequestMetrics
specifier|public
class|class
name|RequestMetrics
block|{
DECL|field|errors
specifier|final
name|Counter1
argument_list|<
name|Integer
argument_list|>
name|errors
decl_stmt|;
DECL|field|successes
specifier|final
name|Counter1
argument_list|<
name|Integer
argument_list|>
name|successes
decl_stmt|;
annotation|@
name|Inject
DECL|method|RequestMetrics (MetricMaker metricMaker)
specifier|public
name|RequestMetrics
parameter_list|(
name|MetricMaker
name|metricMaker
parameter_list|)
block|{
name|Field
argument_list|<
name|Integer
argument_list|>
name|statusCodeField
init|=
name|Field
operator|.
name|ofInteger
argument_list|(
literal|"status"
argument_list|)
operator|.
name|description
argument_list|(
literal|"HTTP status code"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|errors
operator|=
name|metricMaker
operator|.
name|newCounter
argument_list|(
literal|"http/server/error_count"
argument_list|,
operator|new
name|Description
argument_list|(
literal|"Rate of REST API error responses"
argument_list|)
operator|.
name|setRate
argument_list|()
operator|.
name|setUnit
argument_list|(
literal|"errors"
argument_list|)
argument_list|,
name|statusCodeField
argument_list|)
expr_stmt|;
name|successes
operator|=
name|metricMaker
operator|.
name|newCounter
argument_list|(
literal|"http/server/success_count"
argument_list|,
operator|new
name|Description
argument_list|(
literal|"Rate of REST API success responses"
argument_list|)
operator|.
name|setRate
argument_list|()
operator|.
name|setUnit
argument_list|(
literal|"successes"
argument_list|)
argument_list|,
name|statusCodeField
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

