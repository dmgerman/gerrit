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

begin_comment
comment|/** Indicates the reference does not exist. */
end_comment

begin_class
DECL|class|NoSuchRefException
specifier|public
class|class
name|NoSuchRefException
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
DECL|method|NoSuchRefException (String ref)
specifier|public
name|NoSuchRefException
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|this
argument_list|(
name|ref
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|NoSuchRefException (String ref, Throwable why)
specifier|public
name|NoSuchRefException
parameter_list|(
name|String
name|ref
parameter_list|,
name|Throwable
name|why
parameter_list|)
block|{
name|super
argument_list|(
name|ref
argument_list|,
name|why
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

