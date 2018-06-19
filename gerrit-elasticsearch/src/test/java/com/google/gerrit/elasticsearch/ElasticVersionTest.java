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
name|Rule
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|rules
operator|.
name|ExpectedException
import|;
end_import

begin_class
DECL|class|ElasticVersionTest
specifier|public
class|class
name|ElasticVersionTest
block|{
DECL|field|exception
annotation|@
name|Rule
specifier|public
name|ExpectedException
name|exception
init|=
name|ExpectedException
operator|.
name|none
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|supportedVersion ()
specifier|public
name|void
name|supportedVersion
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|ElasticVersion
operator|.
name|forVersion
argument_list|(
literal|"2.4.0"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ElasticVersion
operator|.
name|V2_4
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ElasticVersion
operator|.
name|forVersion
argument_list|(
literal|"2.4.6"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ElasticVersion
operator|.
name|V2_4
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ElasticVersion
operator|.
name|forVersion
argument_list|(
literal|"5.6.0"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ElasticVersion
operator|.
name|V5_6
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ElasticVersion
operator|.
name|forVersion
argument_list|(
literal|"5.6.9"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ElasticVersion
operator|.
name|V5_6
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ElasticVersion
operator|.
name|forVersion
argument_list|(
literal|"5.6.10"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ElasticVersion
operator|.
name|V5_6
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ElasticVersion
operator|.
name|forVersion
argument_list|(
literal|"6.2.0"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ElasticVersion
operator|.
name|V6_2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ElasticVersion
operator|.
name|forVersion
argument_list|(
literal|"6.2.4"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ElasticVersion
operator|.
name|V6_2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ElasticVersion
operator|.
name|forVersion
argument_list|(
literal|"6.3.0"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ElasticVersion
operator|.
name|V6_3
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ElasticVersion
operator|.
name|forVersion
argument_list|(
literal|"6.3.1"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ElasticVersion
operator|.
name|V6_3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|unsupportedVersion ()
specifier|public
name|void
name|unsupportedVersion
parameter_list|()
throws|throws
name|Exception
block|{
name|exception
operator|.
name|expect
argument_list|(
name|ElasticVersion
operator|.
name|InvalidVersion
operator|.
name|class
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"Invalid version: [4.0.0]. Supported versions: "
operator|+
name|ElasticVersion
operator|.
name|supportedVersions
argument_list|()
argument_list|)
expr_stmt|;
name|ElasticVersion
operator|.
name|forVersion
argument_list|(
literal|"4.0.0"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

