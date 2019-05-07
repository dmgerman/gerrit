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
DECL|package|com.google.gerrit.server.rules
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|rules
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
DECL|class|PrologRuleEvaluatorTest
specifier|public
class|class
name|PrologRuleEvaluatorTest
block|{
annotation|@
name|Test
DECL|method|validLabelNamesAreKept ()
specifier|public
name|void
name|validLabelNamesAreKept
parameter_list|()
block|{
for|for
control|(
name|String
name|labelName
range|:
operator|new
name|String
index|[]
block|{
literal|"Verified"
block|,
literal|"Code-Review"
block|}
control|)
block|{
name|assertThat
argument_list|(
name|PrologRuleEvaluator
operator|.
name|checkLabelName
argument_list|(
name|labelName
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|labelName
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|labelWithSpacesIsTransformed ()
specifier|public
name|void
name|labelWithSpacesIsTransformed
parameter_list|()
block|{
name|assertThat
argument_list|(
name|PrologRuleEvaluator
operator|.
name|checkLabelName
argument_list|(
literal|"Label with spaces"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Invalid-Prolog-Rules-Label-Name-Labelwithspaces"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|labelStartingWithADashIsTransformed ()
specifier|public
name|void
name|labelStartingWithADashIsTransformed
parameter_list|()
block|{
name|assertThat
argument_list|(
name|PrologRuleEvaluator
operator|.
name|checkLabelName
argument_list|(
literal|"-dashed-label"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Invalid-Prolog-Rules-Label-Name-dashed-label"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|labelWithInvalidCharactersIsTransformed ()
specifier|public
name|void
name|labelWithInvalidCharactersIsTransformed
parameter_list|()
block|{
name|assertThat
argument_list|(
name|PrologRuleEvaluator
operator|.
name|checkLabelName
argument_list|(
literal|"*urgent*"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Invalid-Prolog-Rules-Label-Name-urgent"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

