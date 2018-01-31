begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.permissions
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|permissions
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
name|MoreObjects
operator|.
name|firstNonNull
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
name|project
operator|.
name|RefPattern
operator|.
name|isRE
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ListMultimap
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
name|Lists
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
name|Maps
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
name|MultimapBuilder
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
name|common
operator|.
name|Nullable
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
name|common
operator|.
name|data
operator|.
name|AccessSection
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
name|common
operator|.
name|data
operator|.
name|Permission
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
name|common
operator|.
name|data
operator|.
name|PermissionRule
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
name|reviewdb
operator|.
name|client
operator|.
name|AccountGroup
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
name|reviewdb
operator|.
name|client
operator|.
name|Project
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
name|CurrentUser
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
name|project
operator|.
name|RefPattern
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
name|project
operator|.
name|RefPatternMatcher
operator|.
name|ExpandParameters
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
name|project
operator|.
name|SectionMatcher
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
name|Inject
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
name|Collections
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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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

begin_comment
comment|/**  * Effective permissions applied to a reference in a project.  *  *<p>A collection may be user specific if a matching {@link AccessSection} uses "${username}" in  * its name. The permissions granted in that section may only be granted to the username that  * appears in the reference name, and also only if the user is a member of the relevant group.  */
end_comment

begin_class
DECL|class|PermissionCollection
specifier|public
class|class
name|PermissionCollection
block|{
annotation|@
name|Singleton
DECL|class|Factory
specifier|public
specifier|static
class|class
name|Factory
block|{
DECL|field|sorter
specifier|private
specifier|final
name|SectionSortCache
name|sorter
decl_stmt|;
annotation|@
name|Inject
DECL|method|Factory (SectionSortCache sorter)
name|Factory
parameter_list|(
name|SectionSortCache
name|sorter
parameter_list|)
block|{
name|this
operator|.
name|sorter
operator|=
name|sorter
expr_stmt|;
block|}
comment|/**      * Get all permissions that apply to a reference. The user is only used for per-user ref names,      * so the return value may include permissions for groups the user is not part of.      *      * @param matcherList collection of sections that should be considered, in priority order      *     (project specific definitions must appear before inherited ones).      * @param ref reference being accessed.      * @param user if the reference is a per-user reference, e.g. access sections using the      *     parameter variable "${username}" will have each username inserted into them to see if      *     they apply to the reference named by {@code ref}.      * @return map of permissions that apply to this reference, keyed by permission name.      */
DECL|method|filter ( Iterable<SectionMatcher> matcherList, String ref, CurrentUser user)
name|PermissionCollection
name|filter
parameter_list|(
name|Iterable
argument_list|<
name|SectionMatcher
argument_list|>
name|matcherList
parameter_list|,
name|String
name|ref
parameter_list|,
name|CurrentUser
name|user
parameter_list|)
block|{
if|if
condition|(
name|isRE
argument_list|(
name|ref
argument_list|)
condition|)
block|{
name|ref
operator|=
name|RefPattern
operator|.
name|shortestExample
argument_list|(
name|ref
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ref
operator|.
name|endsWith
argument_list|(
literal|"/*"
argument_list|)
condition|)
block|{
name|ref
operator|=
name|ref
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|ref
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|boolean
name|perUser
init|=
literal|false
decl_stmt|;
name|Map
argument_list|<
name|AccessSection
argument_list|,
name|Project
operator|.
name|NameKey
argument_list|>
name|sectionToProject
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|SectionMatcher
name|sm
range|:
name|matcherList
control|)
block|{
comment|// If the matcher has to expand parameters and its prefix matches the
comment|// reference there is a very good chance the reference is actually user
comment|// specific, even if the matcher does not match the reference. Since its
comment|// difficult to prove this is true all of the time, use an approximation
comment|// to prevent reuse of collections across users accessing the same
comment|// reference at the same time.
comment|//
comment|// This check usually gets caching right, as most per-user references
comment|// use a common prefix like "refs/sandbox/" or "refs/heads/users/"
comment|// that will never be shared with non-user references, and the per-user
comment|// references are usually less frequent than the non-user references.
comment|//
if|if
condition|(
name|sm
operator|.
name|getMatcher
argument_list|()
operator|instanceof
name|ExpandParameters
condition|)
block|{
if|if
condition|(
operator|!
operator|(
operator|(
name|ExpandParameters
operator|)
name|sm
operator|.
name|getMatcher
argument_list|()
operator|)
operator|.
name|matchPrefix
argument_list|(
name|ref
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|perUser
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|sm
operator|.
name|match
argument_list|(
name|ref
argument_list|,
name|user
argument_list|)
condition|)
block|{
name|sectionToProject
operator|.
name|put
argument_list|(
name|sm
operator|.
name|getSection
argument_list|()
argument_list|,
name|sm
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|sm
operator|.
name|match
argument_list|(
name|ref
argument_list|,
literal|null
argument_list|)
condition|)
block|{
name|sectionToProject
operator|.
name|put
argument_list|(
name|sm
operator|.
name|getSection
argument_list|()
argument_list|,
name|sm
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|List
argument_list|<
name|AccessSection
argument_list|>
name|sections
init|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|sectionToProject
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
name|sorter
operator|.
name|sort
argument_list|(
name|ref
argument_list|,
name|sections
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|SeenRule
argument_list|>
name|seen
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|exclusiveGroupPermissions
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|PermissionRule
argument_list|>
argument_list|>
name|permissions
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|PermissionRule
argument_list|>
argument_list|>
name|overridden
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|PermissionRule
argument_list|,
name|ProjectRef
argument_list|>
name|ruleProps
init|=
name|Maps
operator|.
name|newIdentityHashMap
argument_list|()
decl_stmt|;
name|ListMultimap
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|String
argument_list|>
name|exclusivePermissionsByProject
init|=
name|MultimapBuilder
operator|.
name|hashKeys
argument_list|()
operator|.
name|arrayListValues
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
for|for
control|(
name|AccessSection
name|section
range|:
name|sections
control|)
block|{
name|Project
operator|.
name|NameKey
name|project
init|=
name|sectionToProject
operator|.
name|get
argument_list|(
name|section
argument_list|)
decl_stmt|;
for|for
control|(
name|Permission
name|permission
range|:
name|section
operator|.
name|getPermissions
argument_list|()
control|)
block|{
name|boolean
name|exclusivePermissionExists
init|=
name|exclusiveGroupPermissions
operator|.
name|contains
argument_list|(
name|permission
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|PermissionRule
name|rule
range|:
name|permission
operator|.
name|getRules
argument_list|()
control|)
block|{
name|SeenRule
name|s
init|=
name|SeenRule
operator|.
name|create
argument_list|(
name|section
argument_list|,
name|permission
argument_list|,
name|rule
argument_list|)
decl_stmt|;
name|boolean
name|addRule
decl_stmt|;
if|if
condition|(
name|rule
operator|.
name|isBlock
argument_list|()
condition|)
block|{
name|addRule
operator|=
operator|!
name|exclusivePermissionsByProject
operator|.
name|containsEntry
argument_list|(
name|project
argument_list|,
name|permission
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|addRule
operator|=
name|seen
operator|.
name|add
argument_list|(
name|s
argument_list|)
operator|&&
operator|!
name|rule
operator|.
name|isDeny
argument_list|()
operator|&&
operator|!
name|exclusivePermissionExists
expr_stmt|;
block|}
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|PermissionRule
argument_list|>
argument_list|>
name|p
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|addRule
condition|)
block|{
name|p
operator|=
name|permissions
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|rule
operator|.
name|isDeny
argument_list|()
operator|&&
operator|!
name|exclusivePermissionExists
condition|)
block|{
name|p
operator|=
name|overridden
expr_stmt|;
block|}
if|if
condition|(
name|p
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|PermissionRule
argument_list|>
name|r
init|=
name|p
operator|.
name|get
argument_list|(
name|permission
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|==
literal|null
condition|)
block|{
name|r
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|p
operator|.
name|put
argument_list|(
name|permission
operator|.
name|getName
argument_list|()
argument_list|,
name|r
argument_list|)
expr_stmt|;
block|}
name|r
operator|.
name|add
argument_list|(
name|rule
argument_list|)
expr_stmt|;
name|ruleProps
operator|.
name|put
argument_list|(
name|rule
argument_list|,
name|ProjectRef
operator|.
name|create
argument_list|(
name|project
argument_list|,
name|section
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|permission
operator|.
name|getExclusiveGroup
argument_list|()
condition|)
block|{
name|exclusivePermissionsByProject
operator|.
name|put
argument_list|(
name|project
argument_list|,
name|permission
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|exclusiveGroupPermissions
operator|.
name|add
argument_list|(
name|permission
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
operator|new
name|PermissionCollection
argument_list|(
name|permissions
argument_list|,
name|overridden
argument_list|,
name|ruleProps
argument_list|,
name|perUser
argument_list|)
return|;
block|}
block|}
DECL|field|rules
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|PermissionRule
argument_list|>
argument_list|>
name|rules
decl_stmt|;
DECL|field|overridden
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|PermissionRule
argument_list|>
argument_list|>
name|overridden
decl_stmt|;
DECL|field|ruleProps
specifier|private
specifier|final
name|Map
argument_list|<
name|PermissionRule
argument_list|,
name|ProjectRef
argument_list|>
name|ruleProps
decl_stmt|;
DECL|field|perUser
specifier|private
specifier|final
name|boolean
name|perUser
decl_stmt|;
DECL|method|PermissionCollection ( Map<String, List<PermissionRule>> rules, Map<String, List<PermissionRule>> overridden, Map<PermissionRule, ProjectRef> ruleProps, boolean perUser)
specifier|private
name|PermissionCollection
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|PermissionRule
argument_list|>
argument_list|>
name|rules
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|PermissionRule
argument_list|>
argument_list|>
name|overridden
parameter_list|,
name|Map
argument_list|<
name|PermissionRule
argument_list|,
name|ProjectRef
argument_list|>
name|ruleProps
parameter_list|,
name|boolean
name|perUser
parameter_list|)
block|{
name|this
operator|.
name|rules
operator|=
name|rules
expr_stmt|;
name|this
operator|.
name|overridden
operator|=
name|overridden
expr_stmt|;
name|this
operator|.
name|ruleProps
operator|=
name|ruleProps
expr_stmt|;
name|this
operator|.
name|perUser
operator|=
name|perUser
expr_stmt|;
block|}
comment|/**    * @return true if a "${username}" pattern might need to be expanded to build this collection,    *     making the results user specific.    */
DECL|method|isUserSpecific ()
specifier|public
name|boolean
name|isUserSpecific
parameter_list|()
block|{
return|return
name|perUser
return|;
block|}
comment|/**    * Obtain all permission rules for a given type of permission.    *    * @param permissionName type of permission.    * @return all rules that apply to this reference, for any group. Never null; the empty list is    *     returned when there are no rules for the requested permission name.    */
DECL|method|getPermission (String permissionName)
specifier|public
name|List
argument_list|<
name|PermissionRule
argument_list|>
name|getPermission
parameter_list|(
name|String
name|permissionName
parameter_list|)
block|{
name|List
argument_list|<
name|PermissionRule
argument_list|>
name|r
init|=
name|rules
operator|.
name|get
argument_list|(
name|permissionName
argument_list|)
decl_stmt|;
return|return
name|r
operator|!=
literal|null
condition|?
name|r
else|:
name|Collections
operator|.
expr|<
name|PermissionRule
operator|>
name|emptyList
argument_list|()
return|;
block|}
DECL|method|getOverridden (String permissionName)
name|List
argument_list|<
name|PermissionRule
argument_list|>
name|getOverridden
parameter_list|(
name|String
name|permissionName
parameter_list|)
block|{
return|return
name|firstNonNull
argument_list|(
name|overridden
operator|.
name|get
argument_list|(
name|permissionName
argument_list|)
argument_list|,
name|Collections
operator|.
expr|<
name|PermissionRule
operator|>
name|emptyList
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getRuleProps (PermissionRule rule)
name|ProjectRef
name|getRuleProps
parameter_list|(
name|PermissionRule
name|rule
parameter_list|)
block|{
return|return
name|ruleProps
operator|.
name|get
argument_list|(
name|rule
argument_list|)
return|;
block|}
comment|/**    * Obtain all declared permission rules that match the reference.    *    * @return all rules. The collection will iterate a permission if it was declared in the project    *     configuration, either directly or inherited. If the project owner did not use a known    *     permission (for example {@link Permission#FORGE_SERVER}, then it will not be represented in    *     the result even if {@link #getPermission(String)} returns an empty list for the same    *     permission.    */
DECL|method|getDeclaredPermissions ()
specifier|public
name|Iterable
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|PermissionRule
argument_list|>
argument_list|>
argument_list|>
name|getDeclaredPermissions
parameter_list|()
block|{
return|return
name|rules
operator|.
name|entrySet
argument_list|()
return|;
block|}
comment|/** Tracks whether or not a permission has been overridden. */
annotation|@
name|AutoValue
DECL|class|SeenRule
specifier|abstract
specifier|static
class|class
name|SeenRule
block|{
DECL|method|refPattern ()
specifier|public
specifier|abstract
name|String
name|refPattern
parameter_list|()
function_decl|;
DECL|method|permissionName ()
specifier|public
specifier|abstract
name|String
name|permissionName
parameter_list|()
function_decl|;
annotation|@
name|Nullable
DECL|method|group ()
specifier|public
specifier|abstract
name|AccountGroup
operator|.
name|UUID
name|group
parameter_list|()
function_decl|;
DECL|method|create ( AccessSection section, Permission permission, @Nullable PermissionRule rule)
specifier|static
name|SeenRule
name|create
parameter_list|(
name|AccessSection
name|section
parameter_list|,
name|Permission
name|permission
parameter_list|,
annotation|@
name|Nullable
name|PermissionRule
name|rule
parameter_list|)
block|{
name|AccountGroup
operator|.
name|UUID
name|group
init|=
name|rule
operator|!=
literal|null
operator|&&
name|rule
operator|.
name|getGroup
argument_list|()
operator|!=
literal|null
condition|?
name|rule
operator|.
name|getGroup
argument_list|()
operator|.
name|getUUID
argument_list|()
else|:
literal|null
decl_stmt|;
return|return
operator|new
name|AutoValue_PermissionCollection_SeenRule
argument_list|(
name|section
operator|.
name|getName
argument_list|()
argument_list|,
name|permission
operator|.
name|getName
argument_list|()
argument_list|,
name|group
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

