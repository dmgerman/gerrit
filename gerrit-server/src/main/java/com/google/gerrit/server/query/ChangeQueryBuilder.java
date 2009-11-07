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
DECL|package|com.google.gerrit.server.query
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
name|RevId
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
name|Singleton
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
name|AbbreviatedObjectId
import|;
end_import

begin_comment
comment|/**  * Parses a query string meant to be applied to change objects.  *<p>  * This class is thread-safe, and may be reused across threads to parse queries.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|ChangeQueryBuilder
specifier|public
class|class
name|ChangeQueryBuilder
extends|extends
name|QueryBuilder
block|{
DECL|field|FIELD_CHANGE
specifier|public
specifier|static
specifier|final
name|String
name|FIELD_CHANGE
init|=
literal|"change"
decl_stmt|;
DECL|field|FIELD_COMMIT
specifier|public
specifier|static
specifier|final
name|String
name|FIELD_COMMIT
init|=
literal|"commit"
decl_stmt|;
DECL|field|FIELD_REVIEWER
specifier|public
specifier|static
specifier|final
name|String
name|FIELD_REVIEWER
init|=
literal|"reviewer"
decl_stmt|;
DECL|field|FIELD_OWNER
specifier|public
specifier|static
specifier|final
name|String
name|FIELD_OWNER
init|=
literal|"owner"
decl_stmt|;
DECL|field|CHANGE_RE
specifier|private
specifier|static
specifier|final
name|String
name|CHANGE_RE
init|=
literal|"^[1-9][0-9]*$"
decl_stmt|;
DECL|field|COMMIT_RE
specifier|private
specifier|static
specifier|final
name|String
name|COMMIT_RE
init|=
literal|"^([0-9a-fA-F]{4,"
operator|+
name|RevId
operator|.
name|LEN
operator|+
literal|"})$"
decl_stmt|;
annotation|@
name|Operator
DECL|method|change (final String value)
specifier|public
name|Predicate
name|change
parameter_list|(
specifier|final
name|String
name|value
parameter_list|)
block|{
name|match
argument_list|(
name|value
argument_list|,
name|CHANGE_RE
argument_list|)
expr_stmt|;
return|return
operator|new
name|OperatorPredicate
argument_list|(
name|FIELD_CHANGE
argument_list|,
name|value
argument_list|)
return|;
block|}
annotation|@
name|Operator
DECL|method|commit (final String value)
specifier|public
name|Predicate
name|commit
parameter_list|(
specifier|final
name|String
name|value
parameter_list|)
block|{
specifier|final
name|AbbreviatedObjectId
name|id
init|=
name|AbbreviatedObjectId
operator|.
name|fromString
argument_list|(
name|value
argument_list|)
decl_stmt|;
return|return
operator|new
name|ObjectIdPredicate
argument_list|(
name|FIELD_COMMIT
argument_list|,
name|id
argument_list|)
return|;
block|}
annotation|@
name|Operator
DECL|method|owner (final String value)
specifier|public
name|Predicate
name|owner
parameter_list|(
specifier|final
name|String
name|value
parameter_list|)
block|{
return|return
operator|new
name|OperatorPredicate
argument_list|(
name|FIELD_OWNER
argument_list|,
name|value
argument_list|)
return|;
block|}
annotation|@
name|Operator
DECL|method|reviewer (final String value)
specifier|public
name|Predicate
name|reviewer
parameter_list|(
specifier|final
name|String
name|value
parameter_list|)
block|{
return|return
operator|new
name|OperatorPredicate
argument_list|(
name|FIELD_REVIEWER
argument_list|,
name|value
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|defaultField (final String value)
specifier|protected
name|Predicate
name|defaultField
parameter_list|(
specifier|final
name|String
name|value
parameter_list|)
throws|throws
name|QueryParseException
block|{
if|if
condition|(
name|value
operator|.
name|matches
argument_list|(
name|CHANGE_RE
argument_list|)
condition|)
block|{
return|return
name|change
argument_list|(
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|value
operator|.
name|matches
argument_list|(
name|COMMIT_RE
argument_list|)
condition|)
block|{
return|return
name|commit
argument_list|(
name|value
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
name|error
argument_list|(
literal|"Unsupported query:"
operator|+
name|value
argument_list|)
throw|;
block|}
block|}
DECL|method|match (String val, String re)
specifier|private
specifier|static
name|void
name|match
parameter_list|(
name|String
name|val
parameter_list|,
name|String
name|re
parameter_list|)
block|{
if|if
condition|(
operator|!
name|val
operator|.
name|matches
argument_list|(
name|re
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid value :"
operator|+
name|val
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

