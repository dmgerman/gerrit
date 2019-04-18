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
DECL|package|com.google.gerrit.exceptions
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|exceptions
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
name|reviewdb
operator|.
name|client
operator|.
name|AccountGroup
import|;
end_import

begin_comment
comment|/** Indicates the account group does not exist. */
end_comment

begin_class
DECL|class|NoSuchGroupException
specifier|public
class|class
name|NoSuchGroupException
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
specifier|public
specifier|static
specifier|final
name|String
name|MESSAGE
init|=
literal|"Group Not Found: "
decl_stmt|;
DECL|method|NoSuchGroupException (AccountGroup.Id key)
specifier|public
name|NoSuchGroupException
parameter_list|(
name|AccountGroup
operator|.
name|Id
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
DECL|method|NoSuchGroupException (AccountGroup.UUID key)
specifier|public
name|NoSuchGroupException
parameter_list|(
name|AccountGroup
operator|.
name|UUID
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
DECL|method|NoSuchGroupException (AccountGroup.Id key, Throwable why)
specifier|public
name|NoSuchGroupException
parameter_list|(
name|AccountGroup
operator|.
name|Id
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
block|}
DECL|method|NoSuchGroupException (AccountGroup.UUID key, Throwable why)
specifier|public
name|NoSuchGroupException
parameter_list|(
name|AccountGroup
operator|.
name|UUID
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
block|}
DECL|method|NoSuchGroupException (AccountGroup.NameKey k, Throwable why)
specifier|public
name|NoSuchGroupException
parameter_list|(
name|AccountGroup
operator|.
name|NameKey
name|k
parameter_list|,
name|Throwable
name|why
parameter_list|)
block|{
name|super
argument_list|(
name|MESSAGE
operator|+
name|k
operator|.
name|toString
argument_list|()
argument_list|,
name|why
argument_list|)
expr_stmt|;
block|}
DECL|method|NoSuchGroupException (String who)
specifier|public
name|NoSuchGroupException
parameter_list|(
name|String
name|who
parameter_list|)
block|{
name|this
argument_list|(
name|who
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|NoSuchGroupException (String who, Throwable why)
specifier|public
name|NoSuchGroupException
parameter_list|(
name|String
name|who
parameter_list|,
name|Throwable
name|why
parameter_list|)
block|{
name|super
argument_list|(
name|MESSAGE
operator|+
name|who
argument_list|,
name|why
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

