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
DECL|package|com.google.gerrit.extensions.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|client
package|;
end_package

begin_enum
DECL|enum|ProjectState
specifier|public
enum|enum
name|ProjectState
block|{
DECL|enumConstant|ACTIVE
name|ACTIVE
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|)
block|,
DECL|enumConstant|READ_ONLY
name|READ_ONLY
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|)
block|,
DECL|enumConstant|HIDDEN
name|HIDDEN
argument_list|(
literal|false
argument_list|,
literal|false
argument_list|)
block|;
DECL|field|permitsRead
specifier|private
specifier|final
name|boolean
name|permitsRead
decl_stmt|;
DECL|field|permitsWrite
specifier|private
specifier|final
name|boolean
name|permitsWrite
decl_stmt|;
DECL|method|ProjectState (boolean permitsRead, boolean permitsWrite)
name|ProjectState
parameter_list|(
name|boolean
name|permitsRead
parameter_list|,
name|boolean
name|permitsWrite
parameter_list|)
block|{
name|this
operator|.
name|permitsRead
operator|=
name|permitsRead
expr_stmt|;
name|this
operator|.
name|permitsWrite
operator|=
name|permitsWrite
expr_stmt|;
block|}
DECL|method|permitsRead ()
specifier|public
name|boolean
name|permitsRead
parameter_list|()
block|{
return|return
name|permitsRead
return|;
block|}
DECL|method|permitsWrite ()
specifier|public
name|boolean
name|permitsWrite
parameter_list|()
block|{
return|return
name|permitsWrite
return|;
block|}
block|}
end_enum

end_unit

