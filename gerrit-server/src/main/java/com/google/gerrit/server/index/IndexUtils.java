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
name|gerrit
operator|.
name|server
operator|.
name|index
operator|.
name|change
operator|.
name|ChangeField
operator|.
name|CHANGE
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|index
operator|.
name|change
operator|.
name|ChangeField
operator|.
name|LEGACY_ID
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|index
operator|.
name|change
operator|.
name|ChangeField
operator|.
name|PROJECT
import|;
end_import

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
name|ImmutableSet
import|;
end_import

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
name|Sets
import|;
end_import

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
name|config
operator|.
name|SitePaths
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|errors
operator|.
name|ConfigInvalidException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
DECL|class|IndexUtils
specifier|public
specifier|final
class|class
name|IndexUtils
block|{
DECL|field|CUSTOM_CHAR_MAPPING
specifier|public
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|CUSTOM_CHAR_MAPPING
init|=
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"_"
argument_list|,
literal|" "
argument_list|,
literal|"."
argument_list|,
literal|" "
argument_list|)
decl_stmt|;
DECL|method|setReady (SitePaths sitePaths, String name, int version, boolean ready)
specifier|public
specifier|static
name|void
name|setReady
parameter_list|(
name|SitePaths
name|sitePaths
parameter_list|,
name|String
name|name
parameter_list|,
name|int
name|version
parameter_list|,
name|boolean
name|ready
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|GerritIndexStatus
name|cfg
init|=
operator|new
name|GerritIndexStatus
argument_list|(
name|sitePaths
argument_list|)
decl_stmt|;
name|cfg
operator|.
name|setReady
argument_list|(
name|name
argument_list|,
name|version
argument_list|,
name|ready
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|save
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConfigInvalidException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|changeFields (QueryOptions opts)
specifier|public
specifier|static
name|Set
argument_list|<
name|String
argument_list|>
name|changeFields
parameter_list|(
name|QueryOptions
name|opts
parameter_list|)
block|{
comment|// Ensure we request enough fields to construct a ChangeData. We need both
comment|// change ID and project, which can either come via the Change field or
comment|// separate fields.
name|Set
argument_list|<
name|String
argument_list|>
name|fs
init|=
name|opts
operator|.
name|fields
argument_list|()
decl_stmt|;
if|if
condition|(
name|fs
operator|.
name|contains
argument_list|(
name|CHANGE
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
comment|// A Change is always sufficient.
return|return
name|fs
return|;
block|}
if|if
condition|(
name|fs
operator|.
name|contains
argument_list|(
name|PROJECT
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|fs
operator|.
name|contains
argument_list|(
name|LEGACY_ID
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|fs
return|;
block|}
return|return
name|Sets
operator|.
name|union
argument_list|(
name|fs
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
name|LEGACY_ID
operator|.
name|getName
argument_list|()
argument_list|,
name|PROJECT
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|IndexUtils ()
specifier|private
name|IndexUtils
parameter_list|()
block|{
comment|// hide default constructor
block|}
block|}
end_class

end_unit

