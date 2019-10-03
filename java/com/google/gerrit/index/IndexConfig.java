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
DECL|package|com.google.gerrit.index
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
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

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Consumer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|IntConsumer
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
name|lib
operator|.
name|Config
import|;
end_import

begin_comment
comment|/**  * Implementation-specific configuration for secondary indexes.  *  *<p>Contains configuration that is tied to a specific index implementation but is otherwise  * global, i.e. not tied to a specific {@link Index} and schema version.  */
end_comment

begin_class
annotation|@
name|AutoValue
DECL|class|IndexConfig
specifier|public
specifier|abstract
class|class
name|IndexConfig
block|{
DECL|field|DEFAULT_MAX_TERMS
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_MAX_TERMS
init|=
literal|1024
decl_stmt|;
DECL|method|createDefault ()
specifier|public
specifier|static
name|IndexConfig
name|createDefault
parameter_list|()
block|{
return|return
name|builder
argument_list|()
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|fromConfig (Config cfg)
specifier|public
specifier|static
name|Builder
name|fromConfig
parameter_list|(
name|Config
name|cfg
parameter_list|)
block|{
name|Builder
name|b
init|=
name|builder
argument_list|()
decl_stmt|;
name|setIfPresent
argument_list|(
name|cfg
argument_list|,
literal|"maxLimit"
argument_list|,
name|b
operator|::
name|maxLimit
argument_list|)
expr_stmt|;
name|setIfPresent
argument_list|(
name|cfg
argument_list|,
literal|"maxPages"
argument_list|,
name|b
operator|::
name|maxPages
argument_list|)
expr_stmt|;
name|setIfPresent
argument_list|(
name|cfg
argument_list|,
literal|"maxTerms"
argument_list|,
name|b
operator|::
name|maxTerms
argument_list|)
expr_stmt|;
name|setTypeOrDefault
argument_list|(
name|cfg
argument_list|,
name|b
operator|::
name|type
argument_list|)
expr_stmt|;
return|return
name|b
return|;
block|}
DECL|method|setIfPresent (Config cfg, String name, IntConsumer setter)
specifier|private
specifier|static
name|void
name|setIfPresent
parameter_list|(
name|Config
name|cfg
parameter_list|,
name|String
name|name
parameter_list|,
name|IntConsumer
name|setter
parameter_list|)
block|{
name|int
name|n
init|=
name|cfg
operator|.
name|getInt
argument_list|(
literal|"index"
argument_list|,
literal|null
argument_list|,
name|name
argument_list|,
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|n
operator|!=
literal|0
condition|)
block|{
name|setter
operator|.
name|accept
argument_list|(
name|n
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setTypeOrDefault (Config cfg, Consumer<String> setter)
specifier|private
specifier|static
name|void
name|setTypeOrDefault
parameter_list|(
name|Config
name|cfg
parameter_list|,
name|Consumer
argument_list|<
name|String
argument_list|>
name|setter
parameter_list|)
block|{
name|String
name|type
init|=
name|cfg
operator|!=
literal|null
condition|?
name|cfg
operator|.
name|getString
argument_list|(
literal|"index"
argument_list|,
literal|null
argument_list|,
literal|"type"
argument_list|)
else|:
literal|null
decl_stmt|;
name|setter
operator|.
name|accept
argument_list|(
operator|new
name|IndexType
argument_list|(
name|type
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|builder ()
specifier|public
specifier|static
name|Builder
name|builder
parameter_list|()
block|{
return|return
operator|new
name|AutoValue_IndexConfig
operator|.
name|Builder
argument_list|()
operator|.
name|maxLimit
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
operator|.
name|maxPages
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
operator|.
name|maxTerms
argument_list|(
name|DEFAULT_MAX_TERMS
argument_list|)
operator|.
name|type
argument_list|(
name|IndexType
operator|.
name|getDefault
argument_list|()
argument_list|)
operator|.
name|separateChangeSubIndexes
argument_list|(
literal|false
argument_list|)
return|;
block|}
annotation|@
name|AutoValue
operator|.
name|Builder
DECL|class|Builder
specifier|public
specifier|abstract
specifier|static
class|class
name|Builder
block|{
DECL|method|maxLimit (int maxLimit)
specifier|public
specifier|abstract
name|Builder
name|maxLimit
parameter_list|(
name|int
name|maxLimit
parameter_list|)
function_decl|;
DECL|method|maxLimit ()
specifier|public
specifier|abstract
name|int
name|maxLimit
parameter_list|()
function_decl|;
DECL|method|maxPages (int maxPages)
specifier|public
specifier|abstract
name|Builder
name|maxPages
parameter_list|(
name|int
name|maxPages
parameter_list|)
function_decl|;
DECL|method|maxPages ()
specifier|public
specifier|abstract
name|int
name|maxPages
parameter_list|()
function_decl|;
DECL|method|maxTerms (int maxTerms)
specifier|public
specifier|abstract
name|Builder
name|maxTerms
parameter_list|(
name|int
name|maxTerms
parameter_list|)
function_decl|;
DECL|method|maxTerms ()
specifier|public
specifier|abstract
name|int
name|maxTerms
parameter_list|()
function_decl|;
DECL|method|type (String type)
specifier|public
specifier|abstract
name|Builder
name|type
parameter_list|(
name|String
name|type
parameter_list|)
function_decl|;
DECL|method|type ()
specifier|public
specifier|abstract
name|String
name|type
parameter_list|()
function_decl|;
DECL|method|separateChangeSubIndexes (boolean separate)
specifier|public
specifier|abstract
name|Builder
name|separateChangeSubIndexes
parameter_list|(
name|boolean
name|separate
parameter_list|)
function_decl|;
DECL|method|autoBuild ()
specifier|abstract
name|IndexConfig
name|autoBuild
parameter_list|()
function_decl|;
DECL|method|build ()
specifier|public
name|IndexConfig
name|build
parameter_list|()
block|{
name|IndexConfig
name|cfg
init|=
name|autoBuild
argument_list|()
decl_stmt|;
name|checkLimit
argument_list|(
name|cfg
operator|.
name|maxLimit
argument_list|()
argument_list|,
literal|"maxLimit"
argument_list|)
expr_stmt|;
name|checkLimit
argument_list|(
name|cfg
operator|.
name|maxPages
argument_list|()
argument_list|,
literal|"maxPages"
argument_list|)
expr_stmt|;
name|checkLimit
argument_list|(
name|cfg
operator|.
name|maxTerms
argument_list|()
argument_list|,
literal|"maxTerms"
argument_list|)
expr_stmt|;
return|return
name|cfg
return|;
block|}
block|}
DECL|method|checkLimit (int limit, String name)
specifier|private
specifier|static
name|void
name|checkLimit
parameter_list|(
name|int
name|limit
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|limit
operator|>
literal|0
argument_list|,
literal|"%s must be positive: %s"
argument_list|,
name|name
argument_list|,
name|limit
argument_list|)
expr_stmt|;
block|}
comment|/**    * @return maximum limit supported by the underlying index, or limited for performance reasons.    */
DECL|method|maxLimit ()
specifier|public
specifier|abstract
name|int
name|maxLimit
parameter_list|()
function_decl|;
comment|/**    * @return maximum number of pages (limit / start) supported by the underlying index, or limited    *     for performance reasons.    */
DECL|method|maxPages ()
specifier|public
specifier|abstract
name|int
name|maxPages
parameter_list|()
function_decl|;
comment|/**    * @return maximum number of total index query terms supported by the underlying index, or limited    *     for performance reasons.    */
DECL|method|maxTerms ()
specifier|public
specifier|abstract
name|int
name|maxTerms
parameter_list|()
function_decl|;
comment|/** @return index type. */
DECL|method|type ()
specifier|public
specifier|abstract
name|String
name|type
parameter_list|()
function_decl|;
comment|/**    * @return whether different subsets of changes may be stored in different physical sub-indexes.    */
DECL|method|separateChangeSubIndexes ()
specifier|public
specifier|abstract
name|boolean
name|separateChangeSubIndexes
parameter_list|()
function_decl|;
block|}
end_class

end_unit

