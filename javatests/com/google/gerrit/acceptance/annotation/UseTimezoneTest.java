begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.annotation
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|annotation
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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|AbstractDaemonTest
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
name|acceptance
operator|.
name|UseTimezone
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
DECL|class|UseTimezoneTest
specifier|public
class|class
name|UseTimezoneTest
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Test
annotation|@
name|UseTimezone
argument_list|(
name|timezone
operator|=
literal|"US/Eastern"
argument_list|)
DECL|method|usEastern ()
specifier|public
name|void
name|usEastern
parameter_list|()
block|{
name|assertThat
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.timezone"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"US/Eastern"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|UseTimezone
argument_list|(
name|timezone
operator|=
literal|"UTC"
argument_list|)
DECL|method|utc ()
specifier|public
name|void
name|utc
parameter_list|()
block|{
name|assertThat
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.timezone"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"UTC"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

