begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.common
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
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
name|auto
operator|.
name|value
operator|.
name|AutoValue
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
name|testing
operator|.
name|GerritBaseTests
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
DECL|class|AutoValueTest
specifier|public
class|class
name|AutoValueTest
extends|extends
name|GerritBaseTests
block|{
annotation|@
name|AutoValue
DECL|class|Auto
specifier|abstract
specifier|static
class|class
name|Auto
block|{
DECL|method|create (String val)
specifier|static
name|Auto
name|create
parameter_list|(
name|String
name|val
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_AutoValueTest_Auto
argument_list|(
name|val
argument_list|)
return|;
block|}
DECL|method|val ()
specifier|abstract
name|String
name|val
parameter_list|()
function_decl|;
block|}
annotation|@
name|Test
DECL|method|autoValue ()
specifier|public
name|void
name|autoValue
parameter_list|()
block|{
name|Auto
name|a
init|=
name|Auto
operator|.
name|create
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|a
operator|.
name|val
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|a
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Auto{val=foo}"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

