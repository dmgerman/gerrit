begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
import|;
end_import

begin_comment
comment|/**  * Implementation-specific configuration for secondary indexes.  *<p>  * Contains configuration that is tied to a specific index implementation but is  * otherwise global, i.e. not tied to a specific {@link ChangeIndex} and schema  * version.  */
end_comment

begin_class
DECL|class|IndexConfig
specifier|public
class|class
name|IndexConfig
block|{
DECL|method|createDefault ()
specifier|public
specifier|static
name|IndexConfig
name|createDefault
parameter_list|()
block|{
return|return
operator|new
name|IndexConfig
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
return|;
block|}
DECL|field|maxLimit
specifier|private
specifier|final
name|int
name|maxLimit
decl_stmt|;
DECL|method|IndexConfig (int maxLimit)
specifier|public
name|IndexConfig
parameter_list|(
name|int
name|maxLimit
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|maxLimit
operator|>
literal|0
argument_list|,
literal|"maxLimit must be positive: %s"
argument_list|,
name|maxLimit
argument_list|)
expr_stmt|;
name|this
operator|.
name|maxLimit
operator|=
name|maxLimit
expr_stmt|;
block|}
DECL|method|getMaxLimit ()
specifier|public
name|int
name|getMaxLimit
parameter_list|()
block|{
return|return
name|maxLimit
return|;
block|}
block|}
end_class

end_unit

