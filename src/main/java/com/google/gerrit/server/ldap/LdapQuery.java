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
DECL|package|com.google.gerrit.server.ldap
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ldap
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|NamingEnumeration
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|NamingException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|directory
operator|.
name|Attribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|directory
operator|.
name|DirContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|directory
operator|.
name|SearchControls
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|directory
operator|.
name|SearchResult
import|;
end_import

begin_comment
comment|/** Supports issuing parameterized queries against an LDAP data source. */
end_comment

begin_class
DECL|class|LdapQuery
class|class
name|LdapQuery
block|{
DECL|enum|SearchScope
specifier|static
enum|enum
name|SearchScope
block|{
comment|// Search only the base DN
comment|//
DECL|enumConstant|OBJECT
name|OBJECT
parameter_list|(
name|SearchControls
operator|.
name|OBJECT_SCOPE
parameter_list|)
operator|,
comment|//
DECL|enumConstant|BASE
constructor|BASE(SearchControls.OBJECT_SCOPE
block|)
enum|,
comment|// Search all entries one level under the base DN
comment|//
comment|// Does not include the base DN, and does not include items below items
comment|// under the base DN.
comment|//
DECL|enumConstant|ONE
name|ONE
parameter_list|(
name|SearchControls
operator|.
name|ONELEVEL_SCOPE
parameter_list|)
operator|,
comment|// Search all entries under the base DN, including the base DN.
comment|//
DECL|enumConstant|SUBTREE
constructor|SUBTREE(SearchControls.SUBTREE_SCOPE
block|)
operator|,
comment|//
DECL|enumConstant|SUB
name|SUB
argument_list|(
name|SearchControls
operator|.
name|SUBTREE_SCOPE
argument_list|)
expr_stmt|;
end_class

begin_decl_stmt
DECL|field|scope
specifier|private
specifier|final
name|int
name|scope
decl_stmt|;
end_decl_stmt

begin_expr_stmt
DECL|method|SearchScope (final int scope)
name|SearchScope
argument_list|(
name|final
name|int
name|scope
argument_list|)
block|{
name|this
operator|.
name|scope
operator|=
name|scope
block|;     }
DECL|method|scope ()
name|int
name|scope
argument_list|()
block|{
return|return
name|scope
return|;
block|}
end_expr_stmt

begin_decl_stmt
unit|}    private
DECL|field|base
specifier|final
name|String
name|base
decl_stmt|;
end_decl_stmt

begin_decl_stmt
DECL|field|searchScope
specifier|private
specifier|final
name|SearchScope
name|searchScope
decl_stmt|;
end_decl_stmt

begin_decl_stmt
DECL|field|pattern
specifier|private
specifier|final
name|String
name|pattern
decl_stmt|;
end_decl_stmt

begin_decl_stmt
DECL|field|patternArgs
specifier|private
specifier|final
name|String
index|[]
name|patternArgs
decl_stmt|;
end_decl_stmt

begin_decl_stmt
DECL|field|returnAttributes
specifier|private
specifier|final
name|String
index|[]
name|returnAttributes
decl_stmt|;
end_decl_stmt

begin_expr_stmt
DECL|method|LdapQuery (final String base, final SearchScope searchScope, final String pattern, final Set<String> returnAttributes)
name|LdapQuery
argument_list|(
name|final
name|String
name|base
argument_list|,
name|final
name|SearchScope
name|searchScope
argument_list|,
name|final
name|String
name|pattern
argument_list|,
name|final
name|Set
argument_list|<
name|String
argument_list|>
name|returnAttributes
argument_list|)
block|{
name|this
operator|.
name|base
operator|=
name|base
block|;
name|this
operator|.
name|searchScope
operator|=
name|searchScope
block|;
name|final
name|StringBuilder
name|p
operator|=
operator|new
name|StringBuilder
argument_list|()
block|;
name|final
name|List
argument_list|<
name|String
argument_list|>
name|a
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
literal|4
argument_list|)
block|;
name|int
name|i
operator|=
literal|0
block|;
while|while
condition|(
name|i
operator|<
name|pattern
operator|.
name|length
argument_list|()
condition|)
block|{
specifier|final
name|int
name|b
init|=
name|pattern
operator|.
name|indexOf
argument_list|(
literal|"${"
argument_list|,
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|b
operator|<
literal|0
condition|)
block|{
break|break;
block|}
name|final
name|int
name|e
operator|=
name|pattern
operator|.
name|indexOf
argument_list|(
literal|"}"
argument_list|,
name|b
operator|+
literal|2
argument_list|)
expr_stmt|;
end_expr_stmt

begin_if
if|if
condition|(
name|e
operator|<
literal|0
condition|)
block|{
break|break;
block|}
end_if

begin_expr_stmt
name|p
operator|.
name|append
argument_list|(
name|pattern
operator|.
name|substring
argument_list|(
name|i
argument_list|,
name|b
argument_list|)
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|p
operator|.
name|append
argument_list|(
literal|"{"
operator|+
name|a
operator|.
name|size
argument_list|()
operator|+
literal|"}"
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|a
operator|.
name|add
argument_list|(
name|pattern
operator|.
name|substring
argument_list|(
name|b
operator|+
literal|2
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|i
operator|=
name|e
operator|+
literal|1
expr_stmt|;
end_expr_stmt

begin_expr_stmt
unit|}     if
operator|(
name|i
operator|<
name|pattern
operator|.
name|length
argument_list|()
operator|)
block|{
name|p
operator|.
name|append
argument_list|(
name|pattern
operator|.
name|substring
argument_list|(
name|i
argument_list|)
argument_list|)
block|;     }
name|this
operator|.
name|pattern
operator|=
name|p
operator|.
name|toString
argument_list|()
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|this
operator|.
name|patternArgs
operator|=
operator|new
name|String
index|[
name|a
operator|.
name|size
argument_list|()
index|]
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|a
operator|.
name|toArray
argument_list|(
name|this
operator|.
name|patternArgs
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|this
operator|.
name|returnAttributes
operator|=
operator|new
name|String
index|[
name|returnAttributes
operator|.
name|size
argument_list|()
index|]
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|returnAttributes
operator|.
name|toArray
argument_list|(
name|this
operator|.
name|returnAttributes
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
unit|}    String
DECL|method|getParameters ()
index|[]
name|getParameters
argument_list|()
block|{
return|return
name|patternArgs
return|;
block|}
end_expr_stmt

begin_function
DECL|method|query (final DirContext ctx, final Map<String, String> params)
name|List
argument_list|<
name|Result
argument_list|>
name|query
parameter_list|(
specifier|final
name|DirContext
name|ctx
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
throws|throws
name|NamingException
block|{
specifier|final
name|SearchControls
name|sc
init|=
operator|new
name|SearchControls
argument_list|()
decl_stmt|;
specifier|final
name|NamingEnumeration
argument_list|<
name|SearchResult
argument_list|>
name|res
decl_stmt|;
name|sc
operator|.
name|setSearchScope
argument_list|(
name|searchScope
operator|.
name|scope
argument_list|()
argument_list|)
expr_stmt|;
name|sc
operator|.
name|setReturningAttributes
argument_list|(
name|returnAttributes
argument_list|)
expr_stmt|;
name|res
operator|=
name|ctx
operator|.
name|search
argument_list|(
name|base
argument_list|,
name|pattern
argument_list|,
name|bind
argument_list|(
name|params
argument_list|)
argument_list|,
name|sc
argument_list|)
expr_stmt|;
try|try
block|{
specifier|final
name|List
argument_list|<
name|Result
argument_list|>
name|r
init|=
operator|new
name|ArrayList
argument_list|<
name|Result
argument_list|>
argument_list|()
decl_stmt|;
while|while
condition|(
name|res
operator|.
name|hasMore
argument_list|()
condition|)
block|{
name|r
operator|.
name|add
argument_list|(
operator|new
name|Result
argument_list|(
name|res
operator|.
name|next
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
finally|finally
block|{
name|res
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_function

begin_function
DECL|method|bind (final Map<String, String> params)
specifier|private
name|String
index|[]
name|bind
parameter_list|(
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
specifier|final
name|String
index|[]
name|r
init|=
operator|new
name|String
index|[
name|patternArgs
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|r
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|r
index|[
name|i
index|]
operator|=
name|params
operator|.
name|get
argument_list|(
name|patternArgs
index|[
name|i
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
name|r
index|[
name|i
index|]
operator|==
literal|null
condition|)
block|{
name|r
index|[
name|i
index|]
operator|=
literal|""
expr_stmt|;
block|}
block|}
return|return
name|r
return|;
block|}
end_function

begin_class
DECL|class|Result
class|class
name|Result
block|{
DECL|field|atts
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|atts
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|Result (final SearchResult sr)
name|Result
parameter_list|(
specifier|final
name|SearchResult
name|sr
parameter_list|)
throws|throws
name|NamingException
block|{
for|for
control|(
specifier|final
name|String
name|attName
range|:
name|returnAttributes
control|)
block|{
specifier|final
name|Attribute
name|a
init|=
name|sr
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|attName
argument_list|)
decl_stmt|;
if|if
condition|(
name|a
operator|!=
literal|null
operator|&&
name|a
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|atts
operator|.
name|put
argument_list|(
name|attName
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|a
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|atts
operator|.
name|put
argument_list|(
literal|"dn"
argument_list|,
name|sr
operator|.
name|getNameInNamespace
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|get (final String attName)
name|String
name|get
parameter_list|(
specifier|final
name|String
name|attName
parameter_list|)
block|{
return|return
name|atts
operator|.
name|get
argument_list|(
name|attName
argument_list|)
return|;
block|}
block|}
end_class

unit|}
end_unit

