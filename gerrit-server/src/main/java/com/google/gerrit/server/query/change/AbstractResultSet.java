begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.query.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|query
operator|.
name|change
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|ResultSet
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

begin_class
DECL|class|AbstractResultSet
specifier|abstract
class|class
name|AbstractResultSet
parameter_list|<
name|T
parameter_list|>
implements|implements
name|ResultSet
argument_list|<
name|T
argument_list|>
block|{
annotation|@
name|Override
DECL|method|toList ()
specifier|public
name|List
argument_list|<
name|T
argument_list|>
name|toList
parameter_list|()
block|{
name|ArrayList
argument_list|<
name|T
argument_list|>
name|r
init|=
operator|new
name|ArrayList
argument_list|<
name|T
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|T
name|t
range|:
name|this
control|)
block|{
name|r
operator|.
name|add
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
block|}
end_class

end_unit

