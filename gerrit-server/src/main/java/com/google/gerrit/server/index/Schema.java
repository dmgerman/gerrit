begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
comment|// limitations under the License.package com.google.gerrit.server.git;
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableMap
import|;
end_import

begin_comment
comment|/** Specific version of a secondary index schema. */
end_comment

begin_class
DECL|class|Schema
specifier|public
class|class
name|Schema
parameter_list|<
name|T
parameter_list|>
block|{
DECL|field|release
specifier|private
specifier|final
name|boolean
name|release
decl_stmt|;
DECL|field|fields
specifier|private
specifier|final
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|FieldDef
argument_list|<
name|T
argument_list|,
name|?
argument_list|>
argument_list|>
name|fields
decl_stmt|;
DECL|field|version
specifier|private
name|int
name|version
decl_stmt|;
DECL|method|Schema (boolean release, Iterable<FieldDef<T, ?>> fields)
specifier|protected
name|Schema
parameter_list|(
name|boolean
name|release
parameter_list|,
name|Iterable
argument_list|<
name|FieldDef
argument_list|<
name|T
argument_list|,
name|?
argument_list|>
argument_list|>
name|fields
parameter_list|)
block|{
name|this
operator|.
name|release
operator|=
name|release
expr_stmt|;
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|String
argument_list|,
name|FieldDef
argument_list|<
name|T
argument_list|,
name|?
argument_list|>
argument_list|>
name|b
init|=
name|ImmutableMap
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|FieldDef
argument_list|<
name|T
argument_list|,
name|?
argument_list|>
name|f
range|:
name|fields
control|)
block|{
name|b
operator|.
name|put
argument_list|(
name|f
operator|.
name|getName
argument_list|()
argument_list|,
name|f
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|fields
operator|=
name|b
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
DECL|method|isRelease ()
specifier|public
specifier|final
name|boolean
name|isRelease
parameter_list|()
block|{
return|return
name|release
return|;
block|}
DECL|method|getVersion ()
specifier|public
specifier|final
name|int
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
DECL|method|getFields ()
specifier|public
specifier|final
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|FieldDef
argument_list|<
name|T
argument_list|,
name|?
argument_list|>
argument_list|>
name|getFields
parameter_list|()
block|{
return|return
name|fields
return|;
block|}
DECL|method|setVersion (int version)
name|void
name|setVersion
parameter_list|(
name|int
name|version
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
block|}
end_class

end_unit

