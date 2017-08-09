begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.testutil
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|testutil
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
name|server
operator|.
name|schema
operator|.
name|UpdateUI
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|StatementExecutor
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

begin_class
DECL|class|TestUpdateUI
specifier|public
class|class
name|TestUpdateUI
implements|implements
name|UpdateUI
block|{
annotation|@
name|Override
DECL|method|message (String msg)
specifier|public
name|void
name|message
parameter_list|(
name|String
name|msg
parameter_list|)
block|{}
annotation|@
name|Override
DECL|method|yesno (boolean def, String msg)
specifier|public
name|boolean
name|yesno
parameter_list|(
name|boolean
name|def
parameter_list|,
name|String
name|msg
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|isBatch ()
specifier|public
name|boolean
name|isBatch
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|pruneSchema (StatementExecutor e, List<String> pruneList)
specifier|public
name|void
name|pruneSchema
parameter_list|(
name|StatementExecutor
name|e
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|pruneList
parameter_list|)
throws|throws
name|OrmException
block|{}
block|}
end_class

end_unit

