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
DECL|package|com.google.gerrit.server.index
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|index
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_comment
comment|/** Per-thread singleton to signal if online reindexing is in progress. */
end_comment

begin_class
DECL|class|OnlineReindexMode
specifier|public
class|class
name|OnlineReindexMode
block|{
DECL|field|isOnlineReindex
specifier|private
specifier|static
name|ThreadLocal
argument_list|<
name|Boolean
argument_list|>
name|isOnlineReindex
init|=
operator|new
name|ThreadLocal
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|isActive ()
specifier|public
specifier|static
name|boolean
name|isActive
parameter_list|()
block|{
return|return
name|Optional
operator|.
name|ofNullable
argument_list|(
name|isOnlineReindex
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|orElse
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|)
return|;
block|}
DECL|method|begin ()
specifier|public
specifier|static
name|void
name|begin
parameter_list|()
block|{
name|isOnlineReindex
operator|.
name|set
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
DECL|method|end ()
specifier|public
specifier|static
name|void
name|end
parameter_list|()
block|{
name|isOnlineReindex
operator|.
name|set
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

