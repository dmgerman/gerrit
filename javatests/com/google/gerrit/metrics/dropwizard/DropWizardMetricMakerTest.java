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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertThat
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|DropWizardMetricMakerTest
specifier|public
class|class
name|DropWizardMetricMakerTest
block|{
DECL|field|metrics
name|DropWizardMetricMaker
name|metrics
init|=
operator|new
name|DropWizardMetricMaker
argument_list|(
literal|null
comment|/* MetricRegistry unused in tests */
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|shouldSanitizeUnwantedChars ()
specifier|public
name|void
name|shouldSanitizeUnwantedChars
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|metrics
operator|.
name|sanitizeMetricName
argument_list|(
literal|"very+confusing$long#metric@net/name^1"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"very_confusing_long_metric_net/name_1"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|metrics
operator|.
name|sanitizeMetricName
argument_list|(
literal|"/metric/submetric"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"_metric/submetric"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldReduceConsecutiveSlashesToOne ()
specifier|public
name|void
name|shouldReduceConsecutiveSlashesToOne
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|metrics
operator|.
name|sanitizeMetricName
argument_list|(
literal|"/metric//submetric1///submetric2/submetric3"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"_metric/submetric1/submetric2/submetric3"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldNotFinishWithSlash ()
specifier|public
name|void
name|shouldNotFinishWithSlash
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|metrics
operator|.
name|sanitizeMetricName
argument_list|(
literal|"metric/"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"metric"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|metrics
operator|.
name|sanitizeMetricName
argument_list|(
literal|"metric//"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"metric"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|metrics
operator|.
name|sanitizeMetricName
argument_list|(
literal|"metric/submetric/"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"metric/submetric"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

