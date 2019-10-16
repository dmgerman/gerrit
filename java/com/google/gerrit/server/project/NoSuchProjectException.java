begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|project
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
name|entities
operator|.
name|Project
import|;
end_import

begin_comment
comment|/** Indicates the project does not exist. */
end_comment

begin_class
DECL|class|NoSuchProjectException
specifier|public
class|class
name|NoSuchProjectException
extends|extends
name|Exception
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|MESSAGE
specifier|private
specifier|static
specifier|final
name|String
name|MESSAGE
init|=
literal|"Project not found: "
decl_stmt|;
DECL|field|project
specifier|private
specifier|final
name|Project
operator|.
name|NameKey
name|project
decl_stmt|;
DECL|method|NoSuchProjectException (Project.NameKey key)
specifier|public
name|NoSuchProjectException
parameter_list|(
name|Project
operator|.
name|NameKey
name|key
parameter_list|)
block|{
name|this
argument_list|(
name|key
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|NoSuchProjectException (Project.NameKey key, Throwable why)
specifier|public
name|NoSuchProjectException
parameter_list|(
name|Project
operator|.
name|NameKey
name|key
parameter_list|,
name|Throwable
name|why
parameter_list|)
block|{
name|super
argument_list|(
name|MESSAGE
operator|+
name|key
operator|.
name|toString
argument_list|()
argument_list|,
name|why
argument_list|)
expr_stmt|;
name|project
operator|=
name|key
expr_stmt|;
block|}
DECL|method|project ()
specifier|public
name|Project
operator|.
name|NameKey
name|project
parameter_list|()
block|{
return|return
name|project
return|;
block|}
block|}
end_class

end_unit

