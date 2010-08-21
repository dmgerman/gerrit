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
name|gerrit
operator|.
name|reviewdb
operator|.
name|ReviewDb
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
name|patch
operator|.
name|PatchListCache
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
name|query
operator|.
name|OperatorPredicate
import|;
end_import

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
name|OrmException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provider
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|PatternSyntaxException
import|;
end_import

begin_class
DECL|class|RegexFilePredicate
class|class
name|RegexFilePredicate
extends|extends
name|OperatorPredicate
argument_list|<
name|ChangeData
argument_list|>
block|{
DECL|field|db
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
decl_stmt|;
DECL|field|cache
specifier|private
specifier|final
name|PatchListCache
name|cache
decl_stmt|;
DECL|field|pattern
specifier|private
specifier|final
name|Pattern
name|pattern
decl_stmt|;
DECL|method|RegexFilePredicate (Provider<ReviewDb> db, PatchListCache plc, String re)
name|RegexFilePredicate
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|PatchListCache
name|plc
parameter_list|,
name|String
name|re
parameter_list|)
block|{
name|super
argument_list|(
name|ChangeQueryBuilder
operator|.
name|FIELD_FILE
argument_list|,
name|re
argument_list|)
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|cache
operator|=
name|plc
expr_stmt|;
try|try
block|{
name|this
operator|.
name|pattern
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|re
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PatternSyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|match (ChangeData object)
specifier|public
name|boolean
name|match
parameter_list|(
name|ChangeData
name|object
parameter_list|)
throws|throws
name|OrmException
block|{
name|Collection
argument_list|<
name|String
argument_list|>
name|files
init|=
name|object
operator|.
name|currentFilePaths
argument_list|(
name|db
argument_list|,
name|cache
argument_list|)
decl_stmt|;
if|if
condition|(
name|files
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|path
range|:
name|files
control|)
block|{
if|if
condition|(
name|pattern
operator|.
name|matcher
argument_list|(
name|path
argument_list|)
operator|.
name|find
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
else|else
block|{
comment|// The ChangeData can't do expensive lookups right now. Bypass
comment|// them and include the result anyway. We might be able to do
comment|// a narrow later on to a smaller set.
comment|//
return|return
literal|true
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getCost ()
specifier|public
name|int
name|getCost
parameter_list|()
block|{
return|return
literal|1
return|;
block|}
block|}
end_class

end_unit

